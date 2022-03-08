package com.zz.check.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 编译相关问题的检测类
 * 
 * @author zys
 *
 */
public class JarCompileCheckUtil {
	
	private static final String SUFFIX_CLASS = ".class";
	private static final String SUFFIX_JAR = ".jar";

	/**
	 * 获取所有编译时版本号大于指定版本号的类文件信息 该类将分析所有jar包中的所有类，有时候一个jar中多个类使用了不同的编译器版本
	 * 
	 * 
	 * @param dirPath
	 *            待检测jar包目录
	 * @param maxVersion
	 *            指定版本号
	 * @return 返回结果说明： 检测到非法class或大于指定版本号的类返回HashMap<String, Hashtable<String,
	 *         Long>> key值为jar包名称 value值为jar包中符合条件的类名和版本号信息
	 */
	public static Map<String, Map<String, Long>> getAllBadVersionClassInfo(String dirPath,
			long maxVersion) {

		Map<String, Map<String, Long>> ret = new HashMap<String, Map<String, Long>>();

		File fileDir = new File(dirPath);
		File[] files = fileDir.listFiles();
		for (File file : files) {
			// 只处理jar文件
			if (file.toString().endsWith(SUFFIX_JAR)) {
				// 获得一个jar文件中的类名称集合
				Map<String, Long> badClasses = getBadVersionClassInfo(file, maxVersion);
				if (!badClasses.isEmpty()) {
					ret.put(file.getName(), badClasses);
				}
			}
		}
		return ret;
	}

	/**
	 * 检查一个jar文件，返回所有非法class文件以及编译时版本号大于指定版本号的class文件信息
	 * 该类将分析jar包中的所有类(有时候一个jar中多个类使用了不同的编译器版本)
	 * 
	 * @param file
	 * @param maxVersion
	 * @return
	 */
	public static Map<String, Long> getBadVersionClassInfo(File file, long maxVersion) {
		Map<String, Long> ret = new HashMap<String, Long>();
		long badClassVersion = 0;// 不能被解析的class的版本号设置为0
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			Enumeration<JarEntry> enu = jarFile.entries();
			JarEntry jarEntry;
			while (enu.hasMoreElements()) {
				jarEntry = enu.nextElement();
				// 以.class 结尾的文件
				if (jarEntry.getName().endsWith(SUFFIX_CLASS)) {
					try {
						InputStream inputStream = jarFile.getInputStream(jarEntry);
						DataInputStream data = new DataInputStream(inputStream);
						// 如果class文件不合法
						if (0xCAFEBABE != data.readInt()) {
							ret.put(jarEntry.getName().replaceAll("/", "."), badClassVersion);
							continue;
						}
						long minor = data.readUnsignedShort();
						long major = data.readUnsignedShort();
						// class编译版本高于指定版本
						if (major + minor > maxVersion) {
							ret.put(jarEntry.getName().replaceAll("/", "."), major + minor);
						}
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
			jarFile.close();
		} catch (IOException e) {
		}
		return ret;
	}

	/**
	 * 获取所有编译时版本号大于指定版本号的jar文件信息 该类不会分析jar包中的所有类
	 * 
	 * 
	 * @param dirPath
	 *            待检测jar包目录
	 * @param maxVersion
	 *            指定版本号
	 * @return 返回结果说明： 所有包含非法类文件或者编译时版本号大于指定版本号jar文件信息 Hashtable<String, Long>
	 *         key值为jar包名称 value值为jar包编译器版本号
	 */
	public static List<Map<String, Long>> getAllBadVersionJarInfo(String dirPath, long maxVersion) {

		List<Map<String, Long>> ret = new ArrayList<Map<String, Long>>();

		File fileDir = new File(dirPath);
		File[] files = fileDir.listFiles();
		for (File file : files) {
			// 只处理jar文件
			if (file.toString().endsWith(SUFFIX_JAR)) {
				// 获得一个jar的检测结果
				Map<String, Long> badjarInfo = getBadVersionJarInfo(file, maxVersion);
				if (!badjarInfo.isEmpty()) {
					ret.add(badjarInfo);
				}
			}
		}
		return ret;
	}

	/**
	 * 检查一个jar文件，遇到非法类文件或者大于指定版本号的类文件则立即返回 该类不会分析jar包中的所有类
	 * 
	 * @param file
	 * @param maxVersion
	 * @return 返回值为hashtable对象，含有key值说明版本有问题，否则说明jar包正常
	 *         如果jar中含有不能解析的类文件，则返回的hashtable的value为0，否则返回的value值为编译器版本号
	 */
	public static Map<String, Long> getBadVersionJarInfo(File file, long maxVersion) {

		Map<String, Long> ret = new HashMap<String, Long>();
		long badClassVersion = 0;// 不能被解析的class的版本号设置为0

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			Enumeration<JarEntry> enu = jarFile.entries();
			JarEntry jarEntry;
			while (enu.hasMoreElements()) {
				jarEntry = enu.nextElement();
				// 以.class 结尾的文件
				if (jarEntry.getName().endsWith(SUFFIX_CLASS)) {
					try {
						InputStream inputStream = jarFile.getInputStream(jarEntry);
						DataInputStream data = new DataInputStream(inputStream);
						// 如果class文件不合法
						if (0xCAFEBABE != data.readInt()) {
							ret.put(file.getName(), badClassVersion);
							return ret;
						}
						long minor = data.readUnsignedShort();
						long major = data.readUnsignedShort();
						// class编译版本高于指定版本
						if (major + minor > maxVersion) {
							ret.put(file.getName(), major + minor);
						}
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
			jarFile.close();
		} catch (IOException e) {
		}
		return ret;
	}

	/**
	 * 取得当前运行时环境的jdk版本号
	 * 
	 * @return
	 */
	public static Long getRutimeVersion() {
		long currentVersion = 0;
		ClassLoader loader = JarCompileCheckUtil.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream("java/lang/Class.class");
		try {
			DataInputStream data = new DataInputStream(inputStream);
			if (0xCAFEBABE != data.readInt()) {
				data.close();
			}else{
				long minor = data.readUnsignedShort();
				long major = data.readUnsignedShort();
				currentVersion = major + minor;
				inputStream.close();
			}
		} catch (IOException e) {
		}
		return currentVersion;
	}

	/**
	 * 获得指定jar文件的编译时版本号
	 * 
	 * @param file
	 * @return 返回0表示类文件不正确
	 */
	public static long getBuildVersion(File file) {
		long currentVersion = 0;
		try {
			JarFile jarFile = new JarFile(file.getPath());
			Enumeration<JarEntry> enu = jarFile.entries();
			while (enu.hasMoreElements()) {
				JarEntry element = enu.nextElement();
				if (element.getName().endsWith(SUFFIX_CLASS)) {

					InputStream in = jarFile.getInputStream(element);
					DataInputStream data = new DataInputStream(in);
					if (0xCAFEBABE != data.readInt()) {
						data.close();
					}else{
						long minor = data.readUnsignedShort();
						long major = data.readUnsignedShort();
						currentVersion = major + minor;
						in.close();
						jarFile.close();
						break;
					}
				}
			}

		} catch (Exception e) {
		}
		return currentVersion;
	}
}
