package com.xl.util;

public class FileUtils {
	
	  //获取文件后缀 包含.
	  public static String getEndName(String path){
	    if(path == null)return null;
	    for(int i=path.length()-1; i>=0; i--){
	      if(path.charAt(i) == '.'){
	        return path.substring(i);
	      }
	      else if(path.charAt(i) == '/' || path.charAt(i) == '\\'){
	        return "";
	      }
	    }
	    return "";
	  }

	  //获取文件名
	  public static String getName(String path){
	    if(path == null)return null;
	    for(int i=path.length()-1; i>=0; i--){
	      if(path.charAt(i)=='/' || path.charAt(i)=='\\'){
	        return path.substring(i+1);
	      }
	    }
	    return "";
	  }

	  //获取文件所在文件夹
	  public static String getDir(String path){
	    if(path == null)return null;
	    for(int i=path.length()-1; i>=0; i--){
	      if(path.charAt(i) == '/' || path.charAt(i) == '\\'){
	        return path.substring(0, i);
	      }
	    }
		return path;
	  }

}
