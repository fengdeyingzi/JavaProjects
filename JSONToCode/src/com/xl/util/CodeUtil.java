package com.xl.util;

public class CodeUtil {
	
	//统计代码行数
	public static int getLines(String text){
		int line = 1;
		for(int i=0;i<text.length();i++){
			char c = text.charAt(i);
			if(c=='\n'){
				line ++;
			}
		}
		return line;
	}

}
