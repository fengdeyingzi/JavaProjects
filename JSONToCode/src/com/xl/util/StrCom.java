package com.xl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StrCom
{
	String compl; //简体
	String simpe; //繁体
	public StrCom()
	{
		try{
		compl = getResource("/compl");
		simpe = getResource("/simpe");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getResource(String name) throws IOException{    
        //返回读取指定资源的输入流    
        InputStream input=this.getClass().getResourceAsStream(name);       
       //InputStream is=当前类.class.getResourceAsStream("XX.config");     
        byte[] buffer = new byte[input.available()];
		input.read(buffer);
		String r0_String = new String(buffer, "UTF-8");
		input.close();
		return r0_String;
          
    } 
	//转换为繁体
	public String toSimple(CharSequence compl)
	{
		StringBuffer buf=new StringBuffer();
		int len=compl.length();
		int index;
		for(int i=0;i<len;i++)
		{
			//获得简体字的位置
			index = turnCompl().indexOf(compl.charAt(i));
    if(index!=-1)
			buf.append( turnSimpe().charAt(index));
		else
			buf.append(compl.charAt(i));
		}
			return buf.toString();
	}
	
	//转换为简体
	public String toCompl(CharSequence simple)
	{
		StringBuffer buf=new StringBuffer();
		int len=simple.length();
		int index;
		for(int i=0;i<len;i++)
		{
			//获得繁体字的位置
			index = turnSimpe().indexOf(simple.charAt(i));
			if(index!=-1)
				buf.append( turnCompl().charAt(index));
			else
				buf.append(simple.charAt(i));
		}
		return buf.toString();
	}
	
	//获取简体字典
	String turnCompl()
	{
		return this.compl;
	}
	
	//获取繁体字典
	String turnSimpe()
	{
		return this.simpe;
	}
	
}
