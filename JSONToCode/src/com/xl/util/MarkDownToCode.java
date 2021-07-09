package com.xl.util;

import com.xl.game.math.Str;

public class MarkDownToCode {
	
	//转换成微信小程序
	/*
	 * URl:
	 * 请求参数：
	 * ***\*返回参数：\****
	 * ##
	 */
	public static String toCode(String markdown){
		System.out.println("接口转代码");
		StringBuffer buffer = new StringBuffer();
		int start = 0;
		int line = 1;
		String title = "";
		String url = "";
		String json = "";
		int type = 0;
		for(int i=1;i<markdown.length();i++){
			char c = markdown.charAt(i);
			if(c == '\n'){
				start = i+1;
				line++;
				switch (type) {
				case 0:
					if(searchTextLine(markdown, i, "URl:")){
						url = getLineTitle(markdown,i);
						url = url.substring(4);
						url = url.trim();
						System.out.println("url");
					}
					
					if(searchTextLine(markdown, i, "请求参数：")){
						type=1;
						System.out.println("参数");
					}
					break;
				case 1:
					json = getJSONCode(markdown,i);
					System.out.println("\n\nnetUtil.post(app.url+\""+url+"\",\n "+json+",\n"+"()=>{\n}\n(res)=>{\n},\n(error)=>{\n});");
					buffer.append("\n\n  //"+title+" "+url+"\n  netUtil.post(app.url+\""+url+"\",\n"
					+"  "+json+",\n"
					+"  ()=>{\n    util.showLoading();\n},\n  (res)=>{\n    util.hideLoading();\n},\n  (error)=>{\n    util.hideLoading();\n});");
					type = 0;
					title = "";
					break;
				default:
					break;
				}
				
				
			}
			else{
				if(searchTextLine(markdown, i, ".")){
					if(title.length()==0){
					title = getLineTitle(markdown, i);
					System.out.println("title"+title);
					}
					else{
						System.out.println("title ...");
					}
				}
			}
		}
		
		String retext = buffer.toString();
		retext = retext.replace("Data:{,", "Data:{");
		retext = retext.replace("Data: { ,", "Data:{");
		return retext;
	}
	
	
	//判断当前行是否存在指定文字
	public static boolean searchTextLine(String text,int index, String searchText){
		String lineText = getLine(text, index);
		return lineText.indexOf(searchText)>=0;
	}
	
	//获取当前行的文字
	public static String getLine(String text,int index){
		int start = index;
		int end = 0;
		System.out.println("sta = "+start);
		for(int i=index;i>=0;i--){
			char c = text.charAt(i);
			if(c=='\n'){
				start = i+1;
				break;
			}
			if(i==1){
				start = i;
			}
		}
		System.out.println("start = "+start);
		for(int i=start;i<text.length();i++){
			char c = text.charAt(i);
			if(c=='\n' || (text.length()-1==i)){
				end = i;
				break;
			}
		}
		System.out.println("substring "+start+" "+end);
		if(end!=0)
		return text.substring(start,end);
		else
			return text.substring(start);
	}
	//从当前位置开始读取json代码
	public static String getJSONCode(String text, int index){
		int start = index;
		int end = 0;
		int leve = 0;
		boolean isLine=false;
		boolean isChar = false;
		StringBuffer buffer = new StringBuffer();
		for(int i=index;i>=0;i++){
			
			char c = text.charAt(i);
			System.out.println(c);
			if(c=='{'){
				start = i+1;
				leve = 1;
				buffer.append(c);
				buffer.append("\n");
				break;
				
			}
		}
		
		for(int i=start;i<text.length();i++){
			char c = text.charAt(i);
			System.out.println("getJSONCode "+c);
			if(c=='{'){
				leve++;
				buffer.append("{");
			}
			else if(c=='}'){
				leve--;
				buffer.append("}");
			}
			else if(c=='['){
				buffer.append('[');
			}
			else if(c==']'){
				buffer.append(']');
			}
			else if(c=='\"'){
				if(!isChar)
				isChar = true;
				else
					isChar = false;
			}
			else if(isChar && ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c=='_'))){
				buffer.append(c);
				isLine = false;
			}
			else if(c==':' || c==' '){
				buffer.append(c);
//				buffer.append("\"\"");
			}
			else if(c=='\n' || c=='/' || Str.checkCh(""+c)){
				isChar = false;
				if(!isLine){
					buffer.append(",\n");
					isLine = true;
				}
				else{
					
				}
				
				
			}
			else if(Str.checkCh(""+c)){
				isChar = false;
			}
			System.out.println("c="+c+" leve="+leve);
			if(leve==0){
				end = i;
				break;
			}
			
			
			if(c=='#' || c=='*'){
				if(leve>0){
					for(int ii=0;ii<leve;ii++){
						
						buffer.append("    }");
					}
				}
				end = i;break;
			}
		}
		System.out.println("getJson "+start+ " "+end);
		return buffer.toString();
	}
	
	//提取当前行的文字 并去除*
	public static String getLineTitle(String text,int index){
		String temp = getLine(text,index);
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<temp.length();i++){
			char c = temp.charAt(i);
			if(c != '*' && c!='\\'){
				buffer.append(c);
			}
			
		}
		return buffer.toString();
	}
	

}
