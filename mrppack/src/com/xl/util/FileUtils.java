package com.xl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

public class FileUtils {

	// 获取文件后缀 包含.
	public static String getEndName(String path) {
		if (path == null)
			return null;
		for (int i = path.length() - 1; i >= 0; i--) {
			if (path.charAt(i) == '.') {
				return path.substring(i);
			} else if (path.charAt(i) == '/' || path.charAt(i) == '\\') {
				return "";
			}
		}
		return "";
	}

	// 获取文件名
	public static String getName(String path) {
		if (path == null)
			return null;
		for (int i = path.length() - 1; i >= 0; i--) {
			if (path.charAt(i) == '/' || path.charAt(i) == '\\') {
				return path.substring(i + 1);
			}
		}
		return "";
	}

	// 获取文件所在文件夹
	public static String getDir(String path) {
		if (path == null)
			return null;
		for (int i = path.length() - 1; i >= 0; i--) {
			if (path.charAt(i) == '/' || path.charAt(i) == '\\') {
				return path.substring(0, i);
			}
		}
		return path;
	}

	/*
	 * 列出目录下所有文件
	 */
	public static Collection<File> listFiles(File file, String[] miniType, boolean ischeck) {
		ArrayList<File> filelist = new ArrayList();
		File[] files = file.listFiles();
		if (files == null) {
			return filelist;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				for (String type : miniType)
					if (files[i].getPath().endsWith(type)) {
						filelist.add(files[i]);
						break;
					}
			} else {
				Collection<File> filelist2 = listFiles(files[i], miniType, ischeck);
				for (File f : filelist2)
					filelist.add(f);
			}
		}
		return filelist;
	}

	public static Collection<File> listFiles(File file, boolean ischeck) {
		ArrayList<File> filelist = new ArrayList();
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				filelist.add(files[i]);
			} else {
				Collection<File> filelist2 = listFiles(files[i], ischeck);
				for (File f : filelist2)
					filelist.add(f);
			}
		}
		return filelist;
	}

	// 批量删除目录下指定格式文件
	public static void removeFiles(File path, String[] name) {
		Collection<File> files = listFiles(path, name, true);
		for (File file : files) {
			file.delete();
		}
	}

	public static void writeText(String filename, String info, String coding) {
		System.out.println("writeText " + filename + " ");
		File file = new File(filename);

		try {
			if (file.isFile()) {
				System.out.println("删除文件");
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(info.getBytes(coding));
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String read(File file, String encoding) throws IOException {
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

	/**
	 *        *  复制单个文件       *  @param  oldPath  String  原文件路径  如：c:/fqf.txt 
	 *      *  @param  newPath  String  复制后路径  如：f:/fqf.txt       *  @return 
	 * boolean      
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			//            int  bytesum  =  0; 
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.isFile()) {// 文件存在时 
				InputStream inStream = new FileInputStream(oldPath);// 读入原文件 
				FileOutputStream out = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				//                int  length; 
				while ((byteread = inStream.read(buffer)) != -1) {
					//                    bytesum  +=  byteread;  //字节数  文件大小 
					//                    System.out.println(bytesum); 
					out.write(buffer, 0, byteread);
				}
				inStream.close();

				out.flush();
				out.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 *        *  复制整个文件夹内容       *  @param  oldPath  String  原文件路径  如：c:/fqf 
	 *      *  @param  newPath  String  复制后路径  如：f:/fqf/ff       *  @return 
	 * boolean      
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			// Log.e("copyFolder", ""+oldPath+" "+newPath);
			new File(newPath).mkdirs();// 如果文件夹不存在  则建立新文件夹 
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹 

					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	public static void copyFile(File file, String newPath) {
		// TODO Auto-generated method stub
		copyFile(file.getPath(), newPath);
	}
	
	public static String getMD5(String path) {
		          BigInteger bi = null;
		          try {
		              byte[] buffer = new byte[8192];
		              int len = 0;
		              MessageDigest md = MessageDigest.getInstance("MD5");
		              File f = new File(path);
		              FileInputStream fis = new FileInputStream(f);
		              while ((len = fis.read(buffer)) != -1) {
		                 md.update(buffer, 0, len);
		             }
		             fis.close();
		             byte[] b = md.digest();
		             bi = new BigInteger(1, b);
		         } catch (NoSuchAlgorithmException e) {
		             e.printStackTrace();
		         } catch (IOException e) {
		             e.printStackTrace();
		         }
		         return bi.toString(16);
		     }

}
