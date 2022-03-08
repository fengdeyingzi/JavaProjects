package com.zz.check.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.zz.check.CheckConfig;

/**
 * jar包冲突检测工具类
 * 
 * @author zys
 *
 */
public class JarConflictDetectUtil {

	CheckConfig checkConfig;

	public JarConflictDetectUtil(CheckConfig checkConfig) {
		this.checkConfig = checkConfig;
	}

	/**
	 * 检测并返回指定目录下所有冲突的jar包信息
	 * 
	 * @param dirPath 目录名称,多目录时以;隔开
	 * @return 返回结果说明： 有冲突时返回HashMap<String, HashSet<String>>
	 *         key值为冲突的jar名称，以逗号隔开 value值为冲突的class名称集合
	 * 
	 */
	public Map<String, Set<String>> getConflict(String dirPath) {

		// 最终返回结果
		Map<String, Set<String>> conflictMap = new HashMap<String, Set<String>>();
		// 用来保存每个jar文件中的类名称
		Map<String, Set<String>> jarClassMap = new HashMap<String, Set<String>>();
		// 存储所有jar中的类名称
		Set<String> allClassSet = new HashSet<String>();

		long classCount = 0;
		List<File> files = getJarFiles(dirPath);
		for (File file : files) {
			// 获得一个jar文件中的类名称集合
			Set<String> classSet = getClassNames(file);
			jarClassMap.put(file.getName(), classSet);
			classCount += classSet.size();
			allClassSet.addAll(classSet);// 去重复
		}

		// 所有jar包类的数量与去重后的类数量不相等，则说明有重复类，继续下面的处理
		if (classCount != allClassSet.size()) {

			String jarNames = null;
			for (String className : allClassSet) {
				// 获取包含指定类名的jar名称
				jarNames = getJarNames(className, jarClassMap);
				// 如果类名在多个jar文件中均存在则说明存在jar包冲突问题
				if (jarNames.contains(",")) {
					// 如果该冲突已经被存储
					if (conflictMap.containsKey(jarNames)) {
						conflictMap.get(jarNames).add(className);
					} else {
						HashSet<String> hashSet = new HashSet<String>();
						hashSet.add(className);
						conflictMap.put(jarNames, hashSet);
					}
				}
			}
		}

		return conflictMap;
	}
	
	/**
	 * 获得目录下的所有jar包
	 * @param dirPath 目录名称，多个目录以;隔开
	 * @return
	 */
	private List<File> getJarFiles(String dirPath) {
		List<File> list = new ArrayList<File>();

		String[] dirs = dirPath.split(";");
		for(String dir : dirs){
			File fileDir = new File(dir);
			File[] files = fileDir.listFiles();
			for (File file : files) {
				// 只处理jar文件
				if (file.toString().endsWith(".jar")) {
					// 忽略的jar文件不检查
					if (!checkConfig.isIgnoreJar(file.getName())) {
						list.add(file);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取包含指定类名的jar包名称
	 * 
	 * @param className
	 * @return 多个jar文件时以逗号隔开
	 */
	private String getJarNames(String className, Map<String, Set<String>> jarClassMap) {
		String jarNames = "";

		for (String jarName : jarClassMap.keySet()) {
			if (jarClassMap.get(jarName).contains(className)) {
				jarNames += jarName + ",";
			}
		}
		if (jarNames.endsWith(",")) {
			jarNames = jarNames.substring(0, jarNames.length() - 1);
		}
		return jarNames;
	}

	/**
	 * 从指定目录下获取包含指定类名的所有jar包名称
	 * 
	 * @param dirPath
	 *            jar包目录名称
	 * @param className
	 *            完整的类名称，例：com.zz.Test.class
	 * @return 多个jar以逗号隔开，无结果返回空字符串
	 */
	public String getJarNames(String className, String dirPath) {
		String jarNames = "";
		Set<String> classSet = new HashSet<String>();
		File fileDir = new File(dirPath);
		File[] files = fileDir.listFiles();
		for (File file : files) {
			// 只处理jar文件
			if (file.toString().toUpperCase().endsWith(".JAR")) {
				// 获得一个jar文件中的类名称集合
				classSet = getClassNames(file);
				if (classSet.contains(className)) {
					jarNames += file.getName() + ",";
				}
			}
		}
		if (jarNames.endsWith(",")) {
			jarNames = jarNames.substring(0, jarNames.length() - 1);
		}
		return jarNames;
	}

	/**
	 * 解析并返回jar文件中的class名称
	 * 
	 * @param jarFile
	 *            jar文件
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Set<String> getClassNames(File file) {
		Set<String> classSet = new HashSet<String>();
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			Enumeration enu = jarFile.entries();
			JarEntry element = null;
			String eleName = "";
			while (enu.hasMoreElements()) {
				element = (JarEntry) enu.nextElement();
				eleName = element.toString();
				// 只检测设定的文件类型
				if (checkConfig.isNeededCheckFileType(eleName)) {
					classSet.add(element.getName().replaceAll("/", "."));
				}
			}
			jarFile.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return classSet;
	}

}
