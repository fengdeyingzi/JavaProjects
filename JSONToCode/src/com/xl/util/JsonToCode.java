package com.xl.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonToCode {
	String textString;
	String codeString;
	String objectNameString;
	public static final int JSON_ARRAY = 1, JSON_OBJECT = 2, JSON_INT = 3, JSON_STRING = 5, JSON_DOUBLE = 4;

	public JsonToCode() {
		this.objectNameString = "jsonObject";
	}
	// 设置代码风格

	// 设置传入的json类名
	public void setJsonObjectName(String name) {
		this.objectNameString = name;
	}
	// 设置空格数

	// 设置json
	public void setJson(String text) {
		this.textString = text;
	}

	private int getJSONType(String value) {
		if (value.startsWith("\"")) {
			return JSON_STRING;
		} else if (value.equals("null")) {
			return JSON_STRING;
		} else if (value.startsWith("[")) {
			return JSON_ARRAY;
		} else if (value.startsWith("{")) {
			return JSON_OBJECT;
		} else if (value.indexOf('.') > 0) {
			return JSON_DOUBLE;
		} else {
			return JSON_INT;
		}

	}

	/**
	 * 将字符串复制到剪切板。
	 */
	public static void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		try {
			clip.setContents(tText, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 去除换行符
	private String deleteEnter(String text) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != '\n' && c != '\r') {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public String getCode() {
		return getCode(objectNameString, textString);
	}

	// 获取一段代码的结束位置
	private int getEndIndex(String text, int start) {
		int leve = 0;
		int index = start;
		char c = 0;
		int type = 0;
		for (int i = start; i < text.length(); i++) {
			c = text.charAt(i);
			switch (type) {
			case 0:
				if (c == '{') {
					leve++;
					type = 1;
				} else if (c == '}') {
					leve--;
				}

				break;
			case 1:
				if (c == '{') {
					leve++;
				}
				if (c == '}') {
					leve--;
					if (leve == 0) {
						return i;
					}
				}

			default:
				break;
			}
		}

		return index;
	}

	public void getCode2(StringBuffer buffer, String objectNameString, Object object) {
		if (objectNameString == null || objectNameString.length() == 0) {
			objectNameString = "jsonObject";
		}
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;
			Set<String> keys = jsonObject.keySet();

			for (String key : keys) {
				System.out.println(key + "\n");
				Object obj_item = jsonObject.get(key);
				if (obj_item instanceof JSONArray) {
					// buffer.append("JSONArray "+key+" =
					// "+objectNameString+".getJSONArray("+key+");\n");
					getCode2(buffer, key, (JSONArray) obj_item);
					// JSONArray array_item = (JSONArray)obj_item;
					// for(int i=0;i<array_item.length();i++){
					// Object arr_obj = array_item.get(i);
					// if(arr_obj instanceof JSONObject){
					// getCode2(buffer,key,arr_obj);
					// }
					// }
				} else if (obj_item instanceof JSONObject) {
					buffer.append("JSONObject " + key + " = " + objectNameString + ".getJSONObject(" + key + ");\n");
					getCode2(buffer, key, jsonObject.getJSONObject(key));
				} else if (obj_item instanceof String) {
					buffer.append("String " + key + " = " + objectNameString + ".getString(" + key + ");\n");
				} else if (obj_item instanceof Integer) {
					buffer.append("int " + key + " = " + objectNameString + ".getInt(" + key + ");\n");
				} else if (obj_item instanceof Long) {
					buffer.append("long " + key + " = " + objectNameString + ".getLong(" + key + ");\n");
				} else if (obj_item instanceof Double) {
					buffer.append("double " + key + " = " + objectNameString + ".getDouble(" + key + ");\n");
				}
			}
		} else if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) object;
			buffer.append("JSONArray " + objectNameString + " = " + objectNameString + ".getJSONArray("
					+ objectNameString + ");\n");
			for (int i = 0; i < jsonArray.length(); i++) {
				Object arr_obj = jsonArray.get(i);
				if (arr_obj instanceof JSONArray) {
					getCode2(buffer, objectNameString + "Array", arr_obj);
				} else if (arr_obj instanceof JSONObject) {
					buffer.append(
							"JSONObject " + objectNameString + "_item = " + objectNameString + ".getJSONObject(0);\n");
					getCode2(buffer, objectNameString + "_item", arr_obj);
				}
				if (i == 0)
					break;
			}
		}

	}
	
	//将数据前缀大写
	public String getUpper(String temp){
		if(temp.length()>=1){
			return temp.toUpperCase().substring(0,1)+temp.substring(1);
		}
		return temp;
	}

	// json转换为模型
	public String getModelJavaString(String spaceString,String objectNameString, Object object) {
		if (objectNameString == null || objectNameString.length() == 0) {
			objectNameString = "jsonObject";
		}
		StringBuffer buffer = new StringBuffer();
		if (object instanceof JSONObject) {
			buffer.append(spaceString+"public class " + getUpper(objectNameString) + " {\n");
			JSONObject jsonObject = (JSONObject) object;
			Set<String> keys = jsonObject.keySet();

			for (String key : keys) {
				System.out.println(key + "\n");
				Object obj_item = jsonObject.get(key);
				if (obj_item instanceof JSONArray) {
					buffer.append(spaceString+" public List<"+getUpper(key)+">"+" "+key+";\n");
					buffer.append(getModelJavaString(spaceString+" ", key, (JSONArray) obj_item)) ;
				} else if (obj_item instanceof JSONObject) {
					buffer.append(spaceString+getUpper(key)+" "+key+";\n");
					buffer.append(getModelJavaString(spaceString+" ", key, jsonObject.getJSONObject(key))) ;
					
				} else if (obj_item instanceof String) {
					buffer.append(spaceString+"public String " + key + ";\n");
				} else if (obj_item instanceof Integer) {
					buffer.append(spaceString+"public int " + key + ";\n");
				} else if (obj_item instanceof Long) {
					buffer.append(spaceString+"public long " + key + ";\n");
				} else if (obj_item instanceof Double) {
					buffer.append(spaceString+"public double " + key + ";\n");
				}
				else if(obj_item instanceof Boolean){
					buffer.append(spaceString+"public boolean " + key + ";\n");
				}
			}
			buffer.append(spaceString+"  public "+getUpper(objectNameString)+" fromJson(JSONObject json){\n");
			for(String key:keys){
				Object obj_item = jsonObject.get(key);
				if (obj_item instanceof JSONArray) {
					buffer.append(spaceString+"  List<"+getUpper(key)+"> "+key+" = new ArrayList<Data>();\n"
				+spaceString+"  if(json.has(\""+key+"\")){\n"
							+spaceString+"  JSONArray arr_temp = json.getJSONArray(\""+key+"\");\n"
							+spaceString+"    for(int i=0;i<arr_temp.length();i++){\n"
							+spaceString+"      JSONObject obj_item = arr_temp.getJSONObject(i);\n"
							+spaceString+"      "+key+".add(new "+getUpper(key)+"().fromJson(obj_item));\n"
							+spaceString+"    }\n"
							+spaceString+"  }\n");
//					buffer.append(spaceString+key+" = "+"json.getJSONArray(\""+key+"\");\n");
				} else if (obj_item instanceof JSONObject) {
					buffer.append(spaceString+"if ("+"json.has(\""+key+"\")"+"){\n");
					buffer.append(spaceString+"  "+key+" = "+"new "+getUpper(key)+"().fromJson(json.getJSONObject(\""+key+"\"));\n");
					buffer.append(spaceString+"\n"+spaceString+"}\n");
				} else if (obj_item instanceof String) {
					buffer.append(spaceString+"  "+key+" = "+"json.optString(\""+key+"\");\n");
				} else if (obj_item instanceof Integer) {
					buffer.append(spaceString+"  "+key+" = "+"json.optInt(\""+key+"\");\n");
				} else if (obj_item instanceof Long) {
					buffer.append(spaceString+"  "+key+" = "+"json.optLong(\""+key+"\");\n");
				} else if (obj_item instanceof Double) {
					buffer.append(spaceString+"  "+key+" = "+"json.optDouble(\""+key+"\");\n");
				} else if(obj_item instanceof Boolean){
					buffer.append(spaceString+" "+key+" = "+"json.optBoolean(\""+key+"\");\n");
				}
				
			}
			buffer.append(spaceString+"  return this;\n");
			buffer.append(spaceString+"  }\n");
			buffer.append(spaceString+"\n"
			+spaceString+"}\n");
		} else if (object instanceof JSONArray) {
//			buffer.append("class "+objectNameString+" {\n");
			JSONArray jsonArray = (JSONArray) object;
			
			for (int i = 0; i < jsonArray.length(); i++) {
				Object arr_obj = jsonArray.get(i);
				if (arr_obj instanceof JSONArray) {
					buffer.append(spaceString+"List<"+getUpper(objectNameString)+"> "+objectNameString+";\n");
					buffer.append(getModelJavaString(spaceString+" ", objectNameString + "Array", arr_obj)) ;
					
				} else if (arr_obj instanceof JSONObject) {
//					buffer.append(spaceString+getUpper(objectNameString)+" "+objectNameString+";\n");
					buffer.append(getModelJavaString(spaceString+" ", objectNameString + "", arr_obj)) ;
				}
				if (i == 0)
					break;
			}
//			buffer.append("\n}\n");
		}
		return buffer.toString();
	}

	// 开始转换
	public String getCode(String objectNameString, String textString) {
		StringBuffer buffer = new StringBuffer();
		int start = 0;
		String name = null;
		String value = null;
		char c = 0;
		int type = 0;
		int end = 0;
		for (int i = 0; i < textString.length(); i++) {
			c = textString.charAt(i);
			// System.out.print("type="+type+" "+c+"\n");
			switch (type) {

			case 0: // {
				if (c == '{') {
					type = 1;
				} else if (c == ',') {
					type = 1;
				} else if (c == '}') {
					type = 0;
				} else if (c == '\"') {
					type = 2;
					start = i + 1;
				}
				break;
			case 1:// "
				if (c == '\"') {
					type = 2;
					start = i + 1;
				}
				break;
			case 2:
				if (c == '\"') {
					name = textString.substring(start, i);
					type = 3;
				}
				break;
			case 3:// :
				if (c == ':') {
					type = 4;
					start = i;
					// name= textString.substring(start,i);
				}
				break;
			case 4:// ,
				if ((c >= '0' && c <= '9') || (c == '\"') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
					start = i;
					type = 5;
				} else if (c == '[') {
					type = 6;
					start = i;
				} else if (c == '{') {
					type = 0;
					end = getEndIndex(textString, i);
					buffer.append(
							"JSONObject " + name + "=" + objectNameString + ".getJSONObject(\"" + name + "\"); \n");
					buffer.append(getCode(name, textString.substring(i, end + 1)));
					i = end;

				}
			case 5:
				if (c == ',' || c == '}') {
					value = textString.substring(start, i);
					int jsontype = getJSONType(value);
					switch (jsontype) {
					case JSON_INT:
						buffer.append("int " + name + "=" + objectNameString + ".getInt(\"" + name + "\"); //" + value);
						buffer.append("\n");
						break;
					case JSON_DOUBLE:
						buffer.append(
								"double " + name + "=" + objectNameString + ".getDouble(\"" + name + "\"); //" + value);
						buffer.append("\n");
						break;
					case JSON_STRING:
						buffer.append(
								"String " + name + "=" + objectNameString + ".getString(\"" + name + "\"); //" + value);
						buffer.append("\n");
						break;

					default:
						break;
					}

					type = 0;

				}
				break;
			case 6:// ]
				if (c == ']') {
					value = textString.substring(start, i);
					type = 0;
					buffer.append("JSONArray " + name + "=" + objectNameString + ".getJSONArray(\"" + name + "\"); //"
							+ deleteEnter(value));
					buffer.append("\n");
				}

				break;
			case 7:
				break;
			default:
				break;
			}
		}
		setSysClipboardText(buffer.toString());
		return buffer.toString();
	}

}
