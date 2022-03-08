package mrpbuilder_java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;


import com.xl.util.FileUtils;

public class MrpBuilder {

	byte[] Magic = new byte[4]; // [0:4] 固定标识'MRPG'
	int FileStart; // [4:8] 文件列表终点位置 文件头的长度+文件列表的长度-8
	int MrpTotalLen; // [8:12] 文件列表起始位置 mrp文件的总长度
	int MRPHeaderSize = 240; // [12:16] 文件头的长度，通常是240，如果有额外数据则需要加上额外数据的长度
	byte[] FileName = new byte[12]; // [16:28] GB2312编码带'\0'
	byte[] DisplayName = new byte[24]; // [28:52] GB2312编码带'\0'
	byte[] AuthStr = new byte[16]; // [52:68]
									// 编译器的授权字符串的第2、4、8、9、11、12、1、7、6位字符重新组合的一个字符串
	int Appid; // [68:72]
	int Version; // [72:76]
	int Flag; // [76:80] 第0位是显示标志， 1-2位是cpu性能要求，所以cpu取值范围是0-3只对展讯有效，
				// 第3位是否是shell启动的标志，0表示start启动，1表示shell启动
	int BuilderVersion; // [80:84] 应该是编译器的版本，从几个mrpbuilder看都是10002
	int Crc32; // [84:88] 整个文件计算crc后写回，计算时此字段的值为0
	byte[] Vendor = new byte[40]; // [88:128] GB2312编码带'\0' 供应商
	byte[] Desc = new byte[64]; // [128:192] GB2312编码带'\0'
	int AppidBE; // [192:196] 大端appid
	int VersionBE; // [196:200] 大端version
	int Reserve2; // [200:204] 保留字段
	int ScreenWidth; // [204:206] 在反编译的mrpbuilder中能看到有屏幕信息的字段，但是在斯凯提供的文档中并没有说明
	int ScreenHeight; // [206:208]
	byte Plat; // [208:209] mtk/mstar填1，spr填2，其它填0
	byte[] Reserve3 = new byte[31]; // [209:240]
	boolean useGZIP = false;// 是否gz压缩
	boolean useBMP565 = false; //压缩图片为bmp565

	ArrayList<FileItem> list_file;
	/*
	 * display path filename appid version vendor description
	 */

	public void setGZIP(boolean use) {
		this.useGZIP = use;
	}
	
	public void setBMP565(boolean use){
		this.useBMP565 = use;
	}

	// 打包
	public void pack(Config config, ArrayList<String> list_file) {
		for (int i = 0; i < list_file.size(); i++) {
			FileItem fileItem = new MrpBuilder().new FileItem();
			String temp = list_file.get(i);

			fileItem.path = list_file.get(i);
			fileItem.filename = FileUtils.getName(temp);
			// 辨别带name的文件
			if(fileItem.path.indexOf('(')>0 && fileItem.path.indexOf(')')>0){
				String tempStr = fileItem.path;
				fileItem.path = tempStr.substring(0,tempStr.indexOf('(')).trim();
				//获取括号里的内容
				String kString = tempStr.substring(tempStr.indexOf('(')+1, tempStr.indexOf(')'));
				String[] items = kString.split(",");
				for(String item:items){
					String[] item_arg = item.split("=");
					
					if(item_arg.length==2){
						if(item_arg[0].trim().equals("name")){
							fileItem.filename = item_arg[1].trim();
						}
					}
				}
			}
			config.list_file.add(fileItem);

		}

		int listLen = 0, dataLen = 0;
		// 计算offset
		for (FileItem item : config.list_file) {
			try {
				item.namesize = item.filename.getBytes("GBK").length;
				if (!new File(item.path).exists()) {
					System.out.println("文件未找到：" + item.path);
				} else {
					
						item.len = (int) new File(item.path).length();
						
						byte[] temp_buf = new byte[item.len];
						try {
							FileInputStream input = new FileInputStream(new File(item.path));
							input.read(temp_buf);
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					    item.buf = temp_buf;
					if(useBMP565 && item.path.toLowerCase().endsWith(".bmp")){
						byte[] bmp565buf = getBmp565(item.buf);
						if(bmp565buf!=null){
							item.buf = bmp565buf;
							item.len = item.buf.length;
						}else{
							System.out.println("bmp565 error: "+item.path);
						}
						
					}
					if(useGZIP){
						item.buf = gzipBytes(item.buf);
						item.len = item.buf.length;
					}
					
					// 每个列表项中由文件名长度、文件名、文件偏移、文件长度、0 组成，数值都是uint32因此需要4*4
					listLen += item.namesize + 1 + 4 * 4;
					dataLen += item.namesize + 1 + 4 * 2 + item.len;

				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 第一个文件数据的开始位置
		int filePos = MRPHeaderSize + listLen;
		FileStart = filePos - 8; // 不明白为什么要减8，但是必需这样做
		MrpTotalLen = MRPHeaderSize + listLen + dataLen;
		// 写入头
		new File(config.path).delete();
		RandomAccessFile output = null;
		try {
			output = new RandomAccessFile(config.path, "rw");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {

			output.write(getGBKBytes(config.Magic, 4));
			output.write(getIntByte(FileStart)); // 文件列表终点位置
			output.write(getIntByte(MrpTotalLen));
			output.write(getIntByte(MRPHeaderSize)); // 文件头长度
			output.write(getGBKBytes(config.FileName, 12));
			output.write(getGBKBytes(config.DisplayName, 24));
			output.write(getGBKBytes(config.AuthStr, 16));
			output.write(getIntByte(config.Appid));
			output.write(getIntByte(config.Version));
			output.write(getIntByte(config.Flag));
			output.write(getIntByte(config.BuilderVersion));
			output.write(getIntByte(0));
			output.write(getGBKBytes(config.Vendor, 40)); // 出品商
			output.write(getGBKBytes(config.Desc, 64));
			output.write(getBigIntByte(config.Appid));
			output.write(getBigIntByte(config.Version)); // 版本id 大端
			output.write(getIntByte(0)); //
			output.write(getShortByte(config.ScreenWidth));
			output.write(getShortByte(config.ScreenHeight));
			byte[] byte_plat = new byte[1];
			byte_plat[0] = config.Plat;
			output.write(byte_plat);
			output.write(getGBKBytes("", 31));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}

		// 写入文件列表
		for (FileItem item : config.list_file) {
			try {
				// 每个文件数据由：文件名长度、文件名、文件大小组成，数值都是uint32因此需要4*2
				filePos += item.namesize + 1 + 4 * 2;
				item.offset = filePos;
				// 下一个文件数据的开始位置
				filePos += item.len;

				output.write(getIntByte(item.namesize + 1));
				output.write(item.filename.getBytes("GBK"));
				output.writeByte(0);
				output.write(getIntByte(item.offset));
				output.write(getIntByte(item.len));
				output.write(getIntByte(0));
				System.out.println("filename:" + item.filename + "    pos=" + filePos + " len=" + item.len);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 写入数据
		for (FileItem item : config.list_file) {
			try {
				output.write(getIntByte(item.namesize + 1));
				output.write(item.filename.getBytes("GBK"));
				output.writeByte(0);
				output.write(getIntByte(item.len));
				System.out.println("写入数据："+item.filename+" offset：" + output.length() + " offset:" + item.offset + " len=" + item.len);
				output.seek(item.offset);
				if(item.buf == null){
					System.out.println(""+item.filename+"写入失败，数据为空");
					output.close();
					System.exit(-1);
				}else
				output.write(item.buf);
				
				

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("写入完成");
	}
	
	private boolean isGz(byte[] data){
		if(data.length<3)return false;
//		System.out.println(String.format("%x %x %x ", data[0], data[1], data[2]));
		return ((data[0]&0xff) == 0x1f && (data[1]&0xff) == 0x8b && (data[2]&0xff) == 0x08);
	}

	// 压缩gz
	public byte[] gzipBytes(byte[] buf) {
		if(isGz(buf)){
			return buf;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			GZIPOutputStream out = null;
			out = new GZIPOutputStream(outputStream);
			out.write(buf, 0, buf.length);
			out.finish();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	//读取一个低位int类型数字
	private int get_int(byte[] buf,int ptr){
	 int num = (buf[ptr]&0xff) | (buf[ptr+1]<<8) | (buf[ptr+2]<<16) | (buf[ptr+3]<<24);
	 return num;
	}

	private static int get_short(byte[] buf,int ptr){
	 int num = (buf[ptr]&0xff) | ((buf[ptr+1]&0xff)<<8);
	 return num;
	}
	
	//将bmp图片转换成bmp565
	public byte[] getBmp565(byte[] bmpbuf){
		byte[] bitmap = null;
		 byte[] bufc = bmpbuf;
		 int ptr=0;
		 int w=0,h=0;
		 int bit=16; //bmp位数
		 int bmpstart=0; //bmp数据位置
		 int iy=0;
		 int i=0;
		 
		 int tcolor = 0;
		 int wsize;
		 int iw;
		 
		 

		 //检测文件头
		 if(bufc[0]=='B' && bufc[1]=='M'){
//		  debug_log("BM\n");
		  ptr = 10;
		  bmpstart = get_int(bufc,ptr);
//		  debug_printf("bmpstart\n");
		  ptr = 18;
		  w = get_int(bufc,ptr);
		  ptr = 22;
		  h = get_int(bufc,ptr);
		  ptr = 28;
		  bit = get_short(bufc,ptr);
		  if(bit == 16){
//		   debug_printf("16位图\n");
			bitmap = new byte[w*h*2];
		   ptr = bmpstart;
//		   bmp->width = w;
//		   bmp->height = h;
//		   bmp->bitmap = (uint16*)mrc_malloc(w*h*2);
		   //复制位图数据
		   
		   for( iy=0;iy<h;iy++){
			System.arraycopy(bmpbuf, ptr+(h-1-iy)*w*2, bitmap, iy*w, w*2);
//		    mrc_memcpy(bmp->bitmap+iy*w, bufc+ptr+(bmp->height-1-iy)*bmp->width*2, w*2);
//		    mrc_printf("复制位图数据%d %d\n",iy, bmp->height-1-iy);
		   }
		   
		  }
		  else if(bit == 24){
			  bitmap = new byte[w*h*2];
//		   debug_printf("当前位图是24位");
		   ptr = bmpstart;
//		   bmp->width = w;
//		   bmp->height = h;
		   //对齐字节
		   wsize = w*3;
		   if(wsize%4!=0) wsize = wsize - wsize%4 + 4;
//		   debug_printf("申请内存\n");
//		   bmp->bitmap = (uint16*)mrc_malloc(w*h*3);
		   //32转16位
		   byte[] buf16 = new byte[w*h*2];
		    byte[] buf24 = new byte[bmpbuf.length-ptr]; // (bmpbuf+ptr);
		    System.arraycopy(bmpbuf, ptr, buf24, 0, buf24.length);
		   for(i=0;i<h;i++){
			   for(iw=0;iw<w;iw++){
				   
		    //BGRA
		     tcolor = (((buf24[i*wsize+iw*3]>>3)&0x1f) 
		    | ((buf24[i*wsize+iw*3+1]<<3)&0x7e0) 
		    | ((buf24[i*wsize+iw*3+2]<<8)&0xf800));
		     buf16[(i*w+iw)*2] = (byte)(tcolor&0xff);
		     buf16[(i*w+iw)*2+1] = (byte)((tcolor&0xff00) >> 8);
			   }
		   }
		   //复制位图数据
		    for(iy=0;iy<h;iy++){
				
		     //debug_printf("复制数据 %d %d\n",iy,bmp->height-1-iy);
			 System.arraycopy(buf16, (h-1-iy)*w*2, bitmap, iy*w*2, w*2);
//		     mrc_memcpy(bmp->bitmap + iy*bmp->width, buf16+(bmp->height-1-iy)*bmp->width,w*2);
		   }
//		   mrc_free(buf16);
		    return bitmap;
		  }
		 }
		 else
		 {
//			 debug_printf("不是bmp图片");
		  return bitmap;
		 }
//		 debug_printf("返回bmp\n");
		 return bitmap;
		
	}

	public void main(String[] args) throws IOException {
		// 读取数据
		String jsonPath = "./pack.json";
		if (args.length > 0)
			jsonPath = args[0];
		File f = new File(this.getClass().getResource("/").getPath());
		System.out.println(f);
		File file = new File(f, jsonPath);
		if (!file.exists()) {
			System.out.println("file not found");
			System.exit(0);
		}
		String jsonText = "";
		try {
			jsonText = readText(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject(jsonText);
		Config config = new MrpBuilder().new Config();
		config.Appid = jsonObject.optInt("appid");
		config.DisplayName = jsonObject.optString("display");
		config.FileName = jsonObject.optString("filename");
		config.path = new File(f, jsonObject.optString("path")).getPath();
		if (config.path.length() == 0)
			config.path = config.FileName;
		config.Version = jsonObject.optInt("version");
		config.Vendor = jsonObject.optString("vendor");
		config.Desc = jsonObject.optString("description");
		config.AuthStr = jsonObject.optString("auth");
		config.Flag = jsonObject.optInt("flag");
		config.ScreenWidth = jsonObject.optInt("screen_width");
		config.ScreenHeight = jsonObject.optInt("screen_height");
		config.Plat = (byte) jsonObject.optInt("platform");
		System.out.println("Vendor " + config.Vendor);
		System.out.println("Desc " + config.Desc);
		JSONArray filesarray = jsonObject.getJSONArray("files");
		config.list_file = new ArrayList<MrpBuilder.FileItem>();
		for (int i = 0; i < filesarray.length(); i++) {
			FileItem fileItem = new MrpBuilder().new FileItem();
			String temp = filesarray.getString(i);
			if (temp.indexOf("=") < 0) {
				fileItem.path = new File(f, temp).getPath();
				fileItem.filename = FileUtils.getName(temp);
			} else {
				String[] temp_items = temp.split("=");
				fileItem.path = new File(f, temp_items[0]).getPath();
				fileItem.filename = FileUtils.getName(temp_items[1]);

			}
			config.list_file.add(fileItem);

		}

		int listLen = 0, dataLen = 0;
		// 计算offset
		for (FileItem item : config.list_file) {
			try {
				item.namesize = item.filename.getBytes("GBK").length;
				if (!new File(item.path).exists()) {
					System.out.println("文件未找到：" + item.path);
				} else {
					item.len = (int) new File(item.path).length();
					// 每个列表项中由文件名长度、文件名、文件偏移、文件长度、0 组成，数值都是uint32因此需要4*4
					listLen += item.namesize + 1 + 4 * 4;
					dataLen += item.namesize + 1 + 4 * 2 + item.len;

				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 第一个文件数据的开始位置
		int filePos = MRPHeaderSize + listLen;
		FileStart = filePos - 8; // 不明白为什么要减8，但是必需这样做
		MrpTotalLen = MRPHeaderSize + listLen + dataLen;
		// 写入头
		new File(config.path).delete();
		RandomAccessFile output = new RandomAccessFile(config.path, "rw");
		try {

			output.write(getGBKBytes(config.Magic, 4));
			output.write(getIntByte(FileStart)); // 文件列表终点位置
			output.write(getIntByte(MrpTotalLen));
			output.write(getIntByte(MRPHeaderSize)); // 文件头长度
			output.write(getGBKBytes(config.FileName, 12));
			output.write(getGBKBytes(config.DisplayName, 24));
			output.write(getGBKBytes(config.AuthStr, 16));
			output.write(getIntByte(config.Appid));
			output.write(getIntByte(config.Version));
			output.write(getIntByte(config.Flag));
			output.write(getIntByte(config.BuilderVersion));
			output.write(getIntByte(0));
			output.write(getGBKBytes(config.Vendor, 40)); // 出品商
			output.write(getGBKBytes(config.Desc, 64));
			output.write(getBigIntByte(config.Appid));
			output.write(getBigIntByte(config.Version)); // 版本id 大端
			output.write(getIntByte(0)); //
			output.write(getShortByte(config.ScreenWidth));
			output.write(getShortByte(config.ScreenHeight));
			byte[] byte_plat = new byte[1];
			byte_plat[0] = config.Plat;
			output.write(byte_plat);
			output.write(getGBKBytes("", 31));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}

		// 写入文件列表
		for (FileItem item : config.list_file) {
			try {
				// 每个文件数据由：文件名长度、文件名、文件大小组成，数值都是uint32因此需要4*2
				filePos += item.namesize + 1 + 4 * 2;
				item.offset = filePos;
				// 下一个文件数据的开始位置
				filePos += item.len;

				output.write(getIntByte(item.namesize + 1));
				output.write(item.filename.getBytes("GBK"));
				output.writeByte(0);
				output.write(getIntByte(item.offset));
				output.write(getIntByte(item.len));
				output.write(getIntByte(0));
				System.out.println("filename:" + item.filename + "    pos=" + filePos + " len=" + item.len);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 写入数据
		for (FileItem item : config.list_file) {
			try {
				output.write(getIntByte(item.namesize + 1));
				output.write(item.filename.getBytes("GBK"));
				output.writeByte(0);
				output.write(getIntByte(item.len));
				System.out.println("写入数据：位置：" + output.length() + " offset:" + item.offset + " len=" + item.len);
				output.seek(item.offset);
				FileInputStream input = new FileInputStream(new File(item.path));
				byte[] temp_buf = new byte[item.len];
				input.read(temp_buf);
				input.close();
				output.write(temp_buf);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		output.close();
		System.out.println("写入完成");

	}

	public static byte[] getIntByte(int number) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (number & 0xff);
		bytes[1] = (byte) ((number >> 8) & 0xff);
		bytes[2] = (byte) ((number >> 16) & 0xff);
		bytes[3] = (byte) ((number >> 24) & 0xff);
		return bytes;
	}

	public static byte[] getShortByte(int number) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (number & 0xff);
		bytes[1] = (byte) ((number >> 8) & 0xff);
		return bytes;
	}

	public static byte[] getBigIntByte(int number) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) (number & 0xff);
		bytes[2] = (byte) ((number >> 8) & 0xff);
		bytes[1] = (byte) ((number >> 16) & 0xff);
		bytes[0] = (byte) ((number >> 24) & 0xff);
		return bytes;
	}

	public static String readText(File file, String encoding) throws IOException {
		String content = "";
		// File file = new File(path);

		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);

			byte[] buf = new byte[input.available()];
			input.read(buf);
			input.close();
			content = new String(buf, encoding);
		}
		return content;
	}

	public static byte[] getGBKBytes(String text, int bytelen) {
		byte[] rebyte = new byte[bytelen];
		try {
			byte[] temp = text.getBytes("GBK");
			for (int i = 0; i < Math.min(temp.length, rebyte.length); i++) {
				rebyte[i] = temp[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rebyte;
	}

	public static byte[] read(File file) throws IOException {
		// String content = "";
		// File file = new File(path);

		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);

			byte[] buf = new byte[input.available()];
			input.read(buf);
			input.close();
			return buf;
		}
		return null;
	}

	public class FileItem {
		public String path;
		public String filename;
		public int namesize;
		public int offset;
		public byte[] buf;
		public int len;
	}

	public class Config {
		public String path = "";
		String Magic = "MRPG"; // [0:4] 固定标识'MRPG'
		int FileStart; // [4:8] 文件头的长度+文件列表的长度-8
		int MrpTotalLen; // [8:12] mrp文件的总长度
		int MRPHeaderSize; // [12:16] 文件头的长度，通常是240，如果有额外数据则需要加上额外数据的长度
		public String FileName; // [16:28] GB2312编码带'\0'
		public String DisplayName; // [28:52] GB2312编码带'\0'
		public String AuthStr; // [52:68]
								// 编译器的授权字符串的第2、4、8、9、11、12、1、7、6位字符重新组合的一个字符串
		public int Appid; // [68:72]
		public int Version; // [72:76]
		public int Flag = 3; // [76:80] 第0位是显示标志，
								// 1-2位是cpu性能要求，所以cpu取值范围是0-3只对展讯有效，
								// 第3位是否是shell启动的标志，0表示start启动，1表示shell启动
		public int BuilderVersion = 10002; // [80:84]
											// 应该是编译器的版本，从几个mrpbuilder看都是10002
		int Crc32; // [84:88] 整个文件计算crc后写回，计算时此字段的值为0
		public String Vendor; // [88:128] GB2312编码带'\0' 供应商
		public String Desc; // [128:192] GB2312编码带'\0'
		// public int AppidBE ; // [192:196] 大端appid
		// public int VersionBE ; // [196:200] 大端version
		public int Reserve2; // [200:204] 保留字段
		public int ScreenWidth; // [204:206]
								// 在反编译的mrpbuilder中能看到有屏幕信息的字段，但是在斯凯提供的文档中并没有说明
		public int ScreenHeight; // [206:208]
		public byte Plat = 1; // [208:209] mtk/mstar填1，spr填2，其它填0
		byte[] Reserve3 = new byte[31]; // [209:240]
		public ArrayList<FileItem> list_file;

		public Config() {
		}
	}

}
