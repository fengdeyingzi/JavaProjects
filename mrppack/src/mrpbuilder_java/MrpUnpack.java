package mrpbuilder_java;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;



public class MrpUnpack {

	byte[] data;
	Config config;

	public MrpUnpack(byte[] data) {
		this.data = data;
		config = new Config();
		updateConfig();
	}

	public MrpUnpack(File file) {
		FileInputStream input;
		config = new Config();
		if(!file.isFile()){
			System.err.println("文件不存在");
		}
		try {
			input = new FileInputStream(file);

			int len = input.available();
			byte[] data = new byte[len];
			input.read(data);
			input.close();
			this.data = data;
			updateConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Config getConfig(){
		return config;
	}
	
	private void updateConfig() {
		config.FileStart = readInt(data, 4);
		config.MrpTotalLen = readInt(data, 8);
		config.MRPHeaderSize = readInt(data, 12);
		config.FileName = readGBKString(data, 16);
		config.DisplayName = readGBKString(data, 28);
		config.AuthStr = readGBKString(data, 52);
		config.Appid = readInt(data, 68);
		config.Version = readInt(data, 72);
		config.Flag = readInt(data, 76);
		config.BuilderVersion = readInt(data, 80);
		config.Crc32 = readInt(data, 84);
		config.Vendor = readGBKString(data, 88);
		config.Desc = readGBKString(data, 128);
		config.AppidBE = readBigInt(data, 192);
		config.VersionBE = readBigInt(data, 196);
		config.Reserve2 = readInt(data, 200);
		config.ScreenWidth = readInt(data, 204);
		config.ScreenHeight = readInt(data, 206);
		config.Plat = data[208];
		System.out.println("文件名:"+config.FileName);
		System.out.println("显示名:"+config.DisplayName);
		System.out.println("appid:"+config.Appid);
		System.out.println("BuildVersion:"+config.BuilderVersion);
		System.out.println("auth:"+config.AuthStr);
		
	}
	
	//解包
	public void unpack(String outDir){
		File file_outDir = new File(outDir);
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
			try {
				
				byte[] temp_data = new byte[fileLen];
				System.arraycopy(data, fileOffset, temp_data, 0, fileLen);
				if(isGz(temp_data)){
					System.out.println("gz解压缩 "+filename);
					unGzipData(temp_data, new File(file_outDir,filename).getAbsolutePath());
				}
				else{
					System.out.println("解包 "+filename);
					outputStream = new FileOutputStream(new File(file_outDir,filename));
					outputStream.write(temp_data);
					outputStream.close();
				}
				
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean isGz(byte[] data){
		if(data.length<3)return false;
		System.out.println(String.format("%x %x %x ", data[0], data[1], data[2]));
		return ((data[0]&0xff) == 0x1f && (data[1]&0xff) == 0x8b && (data[2]&0xff) == 0x08);
	}
	
	private int readInt(byte[] temp, int offset){
		int num = 0;
		num|= 0xff & temp[offset];
		num|= (0xff & temp[offset+1])<<8;
		num|= (0xff & temp[offset+2])<<16;
		num|= (0xff & temp[offset+3])<<24;
		return num;
	}
	
	private int readBigInt(byte[] temp, int offset){
		int num = 0;
		num|= 0xff & temp[offset+3];
		num|= (0xff & temp[offset+2])<<8;
		num|= (0xff & temp[offset+1])<<16;
		num|= (0xff & temp[offset])<<24;
		return num;
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
	
	public static void unGzipFile(String sourcedir) {
        String ouputfile = "";
        try {
            //建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourcedir);
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            //建立解压文件输出流
            ouputfile = sourcedir.substring(0,sourcedir.lastIndexOf('.'));
            ouputfile = ouputfile.substring(0,ouputfile.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(ouputfile);

            int num;
            byte[] buf=new byte[1024];

            while ((num = gzin.read(buf,0,buf.length)) != -1)
            {
                fout.write(buf,0,num);
            }

            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex){
            System.err.println(ex.toString());
        }
        return;
    }
	
	public static void unGzipData(byte[] data, String outFile) {
        String ouputfile = "";
        try {
            //建立gzip压缩文件输入流
            InputStream fin = new ByteArrayInputStream(data);
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            //建立解压文件输出流
//            ouputfile = sourcedir.substring(0,sourcedir.lastIndexOf('.'));
//            ouputfile = ouputfile.substring(0,ouputfile.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(new File(outFile));

            int num;
            byte[] buf=new byte[1024];

            while ((num = gzin.read(buf,0,buf.length)) != -1)
            {
                fout.write(buf,0,num);
            }

            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex){
            System.err.println(ex.toString());
        }
        return;
    }

	public class Config {
		public String path = "";
		String Magic = "MRPG"; // [0:4] 固定标识'MRPG'
		int FileStart; // [4:8] 文件头的长度+文件列表的长度-8
		public int MrpTotalLen; // [8:12] mrp文件的总长度
		int MRPHeaderSize; // [12:16] 文件头的长度，通常是240，如果有额外数据则需要加上额外数据的长度
		public String FileName; // [16:28] GB2312编码带'\0'
		public String DisplayName; // [28:52] GB2312编码带'\0'
		public String AuthStr; // [52:68] 编译器的授权字符串的第2、4、8、9、11、12、1、7、6位字符重新组合的一个字符串
		public int Appid; // [68:72]
		public int Version; // [72:76]
		public int Flag = 3; // [76:80] 第0位是显示标志， 1-2位是cpu性能要求，所以cpu取值范围是0-3只对展讯有效，
						// 第3位是否是shell启动的标志，0表示start启动，1表示shell启动
		int BuilderVersion = 10002; // [80:84] 应该是编译器的版本，从几个mrpbuilder看都是10002
		int Crc32; // [84:88] 整个文件计算crc后写回，计算时此字段的值为0
		public String Vendor; // [88:128] GB2312编码带'\0' 供应商
		public String Desc; // [128:192] GB2312编码带'\0'
		int AppidBE; // [192:196] 大端appid
		int VersionBE; // [196:200] 大端version
		int Reserve2; // [200:204] 保留字段
		public int ScreenWidth; // [204:206]
							// 在反编译的mrpbuilder中能看到有屏幕信息的字段，但是在斯凯提供的文档中并没有说明
		public int ScreenHeight; // [206:208]
		public byte Plat = 1; // [208:209] mtk/mstar填1，spr填2，其它填0
		byte[] Reserve3 = new byte[31]; // [209:240]
		ArrayList<MrpBuilder.FileItem> list_file;

		public Config() {
		}
	}
}
