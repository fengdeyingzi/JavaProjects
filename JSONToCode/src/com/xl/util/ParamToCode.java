package com.xl.util;

/*
 * 企查查 接口转代码
 */
public class ParamToCode {

	public static String getCode(String text){
		String items[] = text.split("\n");
		String head = "";
		String key = "";
		String word = "";
		StringBuffer buf = new StringBuffer();
		buf.append("  var text = \"\";\r\n");
		for(int i=0;i<items.length;i++){
			String param = items[i];
			if(param.indexOf("：")>0){
				head = param.split("：")[0];
				i+=3;
			}
			else if(param.equals("String") || param.equals("Integer")){
				buf.append("  text += \""+items[i+1]+"：\"+res."+head+"."+items[i-1]+";  "+"//"+items[i+1]+"\r\n");
				buf.append("  text += \"\\n\";\n");
			}
			else if(param.equals("Object")){
				buf.append(""+items[i-1]);
			}
		}
		return buf.toString();
	}
}
