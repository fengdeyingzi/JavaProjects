package com.zz.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CheckConfig {

	// 包名以.分隔
	private List<String> ignorePackages = new ArrayList<String>();
	// 包名中以/分隔
	private List<String> ignorePackages2 = new ArrayList<String>();
	private List<String> ignoreJars = new ArrayList<String>();

	// 需要检测的文件类型(默认只检测class文件)
	private String[] conflictTypes = new String[] { ".class" };

	public CheckConfig() {
		setCheckConfigByProperty("com.zz.check.CheckConfig");
	}

	/**
	 * 是否是需要忽略的package
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean isIgnorePackage(String packageName) {

		// 包路径以/分隔
		for (String ignorePackage : ignorePackages2) {
			if (packageName.startsWith(ignorePackage)) {
				return true;
			}
		}
		// 包路径以.分隔
		for (String ignorePackage : ignorePackages) {
			if (packageName.startsWith(ignorePackage)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是需要忽略的class
	 * 
	 * @param className
	 * @return
	 */
	public boolean isIgnoreClass(String className) {
		return isIgnorePackage(className);
	}

	/**
	 * 是否是需要检测的冲突文件类型
	 * 
	 * @param filename
	 * @return
	 */
	public boolean isNeededCheckFileType(String filename) {
		for (String type : conflictTypes) {
			if (filename.endsWith(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是需要忽略的jar包
	 * 
	 * @param jarName
	 * @return
	 */
	public boolean isIgnoreJar(String jarName) {
		for (String ignorejar : ignoreJars) {
			if (jarName.startsWith(ignorejar)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过指定properties文件路径来设置需要忽略的包和jar
	 * 
	 * @param propertyFilePath
	 */
	public void setCheckConfig(String propertyFilePath) {
		setCheckConfigByProperty(propertyFilePath);
	}

	/**
	 * 设置需要忽略的包和jar
	 * 
	 * @param ignorePackagesStr
	 *            需要忽略的包名，包名路径以.隔开,多个包以;隔开
	 * @param ignoreJarsStr
	 *            需要忽略的jar名称,以;隔开
	 * @param conflictFileTypes
	 *            需要检测的文件类型，以;隔开
	 * 
	 */
	public void setCheckConfig(String ignorePackagesStr, String ignoreJarsStr, String conflictFileTypes) {
		if (ignorePackagesStr != null && !"".equals(ignorePackagesStr)) {
			ignorePackagesStr = ignorePackagesStr.replace("*", "");
			if (!"".equals(ignorePackagesStr)) {
				ignorePackages = Arrays.asList(ignorePackagesStr.split(";"));
			}
			ignorePackagesStr = ignorePackagesStr.replace(".", "/");
			if (!"".equals(ignorePackagesStr)) {
				ignorePackages2 = Arrays.asList(ignorePackagesStr.split(";"));
			}
		}
		if (ignoreJarsStr != null && !"".equals(ignoreJarsStr)) {
			ignoreJarsStr = ignoreJarsStr.replace("*", "").replace(".jar", "");
			if (!"".equals(ignoreJarsStr)) {
				ignoreJars = Arrays.asList(ignoreJarsStr.split(";"));
			}
		}

		if (conflictFileTypes != null && !"".equals(conflictFileTypes)) {
			conflictTypes = conflictFileTypes.split(";");
		}
	}

	private void setCheckConfigByProperty(String propertyFilePath) {
		ResourceBundle localResourceBundle = ResourceBundle.getBundle(propertyFilePath);
		String ignorePackagesString = localResourceBundle.getString("ignorePackages");
		String ignoreJarsString = localResourceBundle.getString("ingoreJars");
		String conflictFileTypes = localResourceBundle.getString("conflict.filetype");
		setCheckConfig(ignorePackagesString, ignoreJarsString, conflictFileTypes);
	}

}
