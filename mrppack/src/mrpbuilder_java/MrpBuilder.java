package mrpbuilder_java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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

	ArrayList<FileItem> list_file;
	/*
	 * display path filename appid version vendor description
	 */

	// 打包
	public void pack(Config config, ArrayList<File> list_file) {
		for (int i = 0; i < list_file.size(); i++) {
			FileItem fileItem = new MrpBuilder().new FileItem();
			String temp = list_file.get(i).getPath();

			fileItem.path = list_file.get(i).getPath();
			fileItem.filename = FileUtils.getName(temp);

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
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rebyte;
	}

	public static byte[] read(File file) throws IOException {
		String content = "";
		// File file = new File(path);

		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);

			byte[] buf = new byte[input.available()];
			input.read(buf);
			return buf;
		}
		return null;
	}

	public class FileItem {
		String path;
		String filename;
		int namesize;
		int offset;
		int len;
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
