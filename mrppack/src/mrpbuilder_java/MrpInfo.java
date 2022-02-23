package mrpbuilder_java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import mrpbuilder_java.MrpBuilder.FileItem;

public class MrpInfo {
	String filepath;

	public MrpInfo(String filepath) {
		this.filepath = filepath;
	}

	public MrpBuilder.Config getInfo() {
		MrpBuilder.Config config = new MrpBuilder().new Config();
		config.list_file = new ArrayList<MrpBuilder.FileItem>();
		byte[] tempbyte = new byte[4];
		byte[] tempdata = new byte[64];
		FileInputStream input;
		try {
			input = new FileInputStream(new File(filepath));
			
			input.read(tempbyte);
			config.FileStart = readInt(input);
			config.MrpTotalLen = readInt(input);
			config.MRPHeaderSize = readInt(input);
			
			config.FileName = readGBKString(input, 12);
			config.DisplayName = readGBKString(input, 24);
			// output.write(getGBKBytes(config.DisplayName, 24));
			config.AuthStr = readGBKString(input, 16);
			// output.write(getGBKBytes(config.AuthStr, 16));
			config.Appid = readInt(input);
			// output.write(getIntByte(config.Appid));
			config.Version = readInt(input);
			// output.write(getIntByte(config.Version));
			config.Flag = readInt(input);
			// output.write(getIntByte(config.Flag));
			config.BuilderVersion = readInt(input);
			// output.write(getIntByte(config.BuilderVersion));
			readInt(input);
			// output.write(getIntByte(0));
			config.Vendor = readGBKString(input, 40);
			// output.write(getGBKBytes(config.Vendor, 40)); // 出品商
			config.Desc = readGBKString(input, 64);
			// output.write(getGBKBytes(config.Desc, 64));
			// readInt(input);
			// readInt(input);
			config.Appid = readBigInt(input);
			config.Version = readBigInt(input);
			readInt(input);
			int tempscr = readInt(input);
			config.ScreenWidth = tempscr & 0xffff;
			config.ScreenHeight = (tempscr >> 16) & 0xffff;
			input.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// output.write(getBigIntByte(config.Appid));
		// output.write(getBigIntByte(config.Version)); // 版本id 大端
		// output.write(getIntByte(0)); //
		// output.write(getShortByte(config.ScreenWidth));
		// output.write(getShortByte(config.ScreenHeight));
		byte[] data = null;
		try {
			input = new FileInputStream(new File(filepath));

			int len = input.available();
			 data = new byte[len];
			input.read(data);
			input.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
			int listStart = config.MRPHeaderSize;
			int listEnd = config.FileStart + 8;
			for(int offset = listStart;offset<listEnd;){
				//文件名长度
				int fileNameLen = readInt(data, offset);
				offset+=4;
				String filename = readGBKString(data, offset);
				System.out.println("filename:"+filename);
				offset+=fileNameLen;
				int fileOffset = readInt(data, offset);
				offset+=4;
				int fileLen = readInt(data, offset);
				offset+=4;
				offset+=4;
				FileOutputStream outputStream;
				byte[] temp_data = new byte[fileLen];
				if(fileLen+fileOffset>data.length){
					System.out.println("文件偏移异常："+filename+" offset:"+fileOffset+",len:"+fileLen);
				}else{
					System.arraycopy(data, fileOffset, temp_data, 0, fileLen);
				}
				FileItem fileItem = new MrpBuilder().new FileItem();
				fileItem.filename = filename;
				fileItem.offset = fileOffset;
				fileItem.len = fileLen;
				
				config.list_file.add(fileItem);
			}
		return config;
	}

	public void setInfo(MrpBuilder.Config config) {
		try {
			RandomAccessFile output = new RandomAccessFile(new File(filepath), "rw");
			output.seek(16);

			// output.write(getIntByte(FileStart)); // 文件列表终点位置
			// output.write(getIntByte(MrpTotalLen));
			// output.write(getIntByte(MRPHeaderSize)); // 文件头长度
			if(config.FileName!=null)
			output.write(MrpBuilder.getGBKBytes(config.FileName, 12));
			output.seek(28);
			if(config.DisplayName!=null)
			output.write(MrpBuilder.getGBKBytes(config.DisplayName, 24));
			output.seek(52);
			if(config.AuthStr!=null)
			output.write(MrpBuilder.getGBKBytes(config.AuthStr, 16));
			output.seek(68);
			if(config.Appid!=0)
			output.write(MrpBuilder.getIntByte(config.Appid));
			output.seek(72);
			if(config.Version!=0)
			output.write(MrpBuilder.getIntByte(config.Version));
			output.seek(76);
			
//			output.write(MrpBuilder.getIntByte(config.Flag));
			output.seek(80);
//			output.write(MrpBuilder.getIntByte(config.BuilderVersion));
			// output.write(getIntByte(0));
			output.seek(88);
if(config.Vendor!=null)
			output.write(MrpBuilder.getGBKBytes(config.Vendor, 40));
			// 出品商
			output.seek(128);
			if(config.Desc!=null)
			output.write(MrpBuilder.getGBKBytes(config.Desc, 64));
			output.seek(192);
			if(config.Appid!=0)
			output.write(MrpBuilder.getBigIntByte(config.Appid));
			output.seek(196);
			if(config.Version!=0)
			output.write(MrpBuilder.getBigIntByte(config.Version)); // 版本id 大端
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// output.write(getIntByte(0)); //
		// output.write(getShortByte(config.ScreenWidth));
		// output.write(getShortByte(config.ScreenHeight));
	}
	
	private String readGBKString(byte[] temp, int offset){
		int len = 0;
		for(int i=offset;i<temp.length;i++){
			if(temp[i]!=0)
			len++;
			else break;
		}
		
		try {
			byte[] bytes = new byte[len];
			System.arraycopy(temp, offset, bytes, 0, len);
			return new String(bytes,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readGBKString(InputStream input, int bytelen) {
		byte[] rebyte = new byte[bytelen];
		try {
			input.read(rebyte);
			int len = 0;
			for (int i = 0; i < rebyte.length; i++) {
				if (rebyte[i] == 0) {
					break;
				}
				len++;
			}

			return new String(rebyte, 0, len, "GBK");
		} catch (IOException e) {
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
		// System.out.println("readint "+rebyte[0]);
		return (rebyte[0] & 0xff) | (((int) (rebyte[1] & 0xff)) << 8) | (((int) (rebyte[2] & 0xff)) << 16)
				| (((int) (rebyte[3] & 0xff)) << 24);
	}
	
	private int readInt(byte[] temp, int offset){
		int num = 0;
		num|= 0xff & temp[offset];
		num|= (0xff & temp[offset+1])<<8;
		num|= (0xff & temp[offset+2])<<16;
		num|= (0xff & temp[offset+3])<<24;
		return num;
	}

	public static int readBigInt(InputStream input) {
		byte[] rebyte = new byte[4];

		try {
			input.read(rebyte);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("readint "+rebyte[0]);
		return (rebyte[3] & 0xff) | (((int) (rebyte[2] & 0xff)) << 8) | (((int) (rebyte[1] & 0xff)) << 16)
				| (((int) (rebyte[0] & 0xff)) << 24);
	}

}
