package mrpbuilder_java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MrpInfo {
	String filepath;
	public MrpInfo(String filepath){
		this.filepath = filepath;
	}
	
	public MrpBuilder.Config getInfo(){
		MrpBuilder.Config config = new MrpBuilder().new Config();
		byte[] tempbyte = new byte[4];
		byte[] tempdata = new byte[64];
		FileInputStream input;
		try {
			input = new FileInputStream(new File(filepath));
		
			input.read(tempbyte);
			input.read(tempbyte); // 文件列表终点位置
			input.read(tempbyte);
			input.read(tempbyte); // 文件头长度
			config.FileName = readGBKString(input, 12);
			config.DisplayName = readGBKString(input, 24);
//			output.write(getGBKBytes(config.DisplayName, 24));
			config.AuthStr = readGBKString(input, 16);
//			output.write(getGBKBytes(config.AuthStr, 16));
			config.Appid = readInt(input);
//			output.write(getIntByte(config.Appid));
			config.Version = readInt(input);
//			output.write(getIntByte(config.Version));
			config.Flag = readInt(input);
//			output.write(getIntByte(config.Flag));
			config.BuilderVersion = readInt(input);
//			output.write(getIntByte(config.BuilderVersion));
			readInt(input);
//			output.write(getIntByte(0));
			config.Vendor = readGBKString(input, 40);
//			output.write(getGBKBytes(config.Vendor, 40)); // 出品商
			config.Desc = readGBKString(input, 64);
//			output.write(getGBKBytes(config.Desc, 64));
//			readInt(input);
//			readInt(input);
			config.Appid = readBigInt(input);
			config.Version = readBigInt(input);
			readInt(input);
			int tempscr = readInt(input);
			config.ScreenWidth = tempscr&0xffff;
			config.ScreenHeight = (tempscr>>16)&0xffff;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		output.write(getBigIntByte(config.Appid));
//		output.write(getBigIntByte(config.Version)); // 版本id 大端
//		output.write(getIntByte(0)); //
//		output.write(getShortByte(config.ScreenWidth));
//		output.write(getShortByte(config.ScreenHeight));
		return config;
	}
	
	
	public static String readGBKString(InputStream input, int bytelen) {
		byte[] rebyte = new byte[bytelen];
		try {
			input.read(rebyte);
		int len = 0;
		for(int i=0;i<rebyte.length;i++){
			if(rebyte[i] == 0){
				break;
			}
			len++;
		}
		
		return new String(rebyte, 0, len, "GBK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
		
	}
	
	public static int readInt(InputStream input) {
		byte[] rebyte = new byte[4];
		
		try {
			input.read(rebyte);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("readint "+rebyte[0]);
		return (rebyte[0]&0xff) | (((int)(rebyte[1]&0xff))<<8) | (((int)(rebyte[2]&0xff))<<16) | (((int)(rebyte[3]&0xff))<<24);
	}
	
	public static int readBigInt(InputStream input) {
		byte[] rebyte = new byte[4];
		
		try {
			input.read(rebyte);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("readint "+rebyte[0]);
		return (rebyte[3]&0xff) | (((int)(rebyte[2]&0xff))<<8) | (((int)(rebyte[1]&0xff))<<16) | (((int)(rebyte[0]&0xff))<<24);
	}

}
