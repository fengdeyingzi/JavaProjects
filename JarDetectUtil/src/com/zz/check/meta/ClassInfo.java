package com.zz.check.meta;

import java.util.HashSet;
import java.util.Set;

public class ClassInfo {

	// 类名称
	private String name;

	// 所属包名称
	private String packageName = "";

	// 所属jar名称
	private String jarName;

	// 该类运行所依赖的类(包含自身jar包中的类)
	private Set<String> dependentClasses = new HashSet<String>();

	// 该类运行所依赖的类和方法
	// private Set<String> dependentClassAndMethods = new HashSet<String>();

	public ClassInfo(String fullClassName, String jarName) {
		// 类名为 com/zz/check/XXXX.class
		name = fullClassName.substring(0, fullClassName.length() - 6).replace("/", ".");
		int n = name.lastIndexOf(".");
		if (n > -1) {
			packageName = name.substring(0, n);
		}
		n = jarName.lastIndexOf("\\");
		if (n > -1) {
			jarName = jarName.substring(n + 1);
		}
		this.jarName = jarName;
	}

	public void addDependentClasses(String className) {
		dependentClasses.add(className.replace("/", "."));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public Set<String> getDependentClasses() {
		dependentClasses.remove(this.name);
		return dependentClasses;
	}

	public String toString() {
		return "[ name = " + name + ", packageName = " + packageName + " , jarName = " + jarName + " ] ";
	}
}
