package com.zz.check.meta;

import java.util.HashSet;
import java.util.Set;

public class JarInfo {

	// jar名称
	private String name;

	// jar编译时jdk版本号
	private long buildVersion;

	// jar中的package
	private Set<String> ownedPackages = new HashSet<String>();

	// jar中的类
	private Set<String> ownedClasses = new HashSet<String>();

	// 运行时所依赖的类
	private Set<String> dependentClasses = new HashSet<String>();

	// 运行时需要依赖的jar包
	private Set<String> dependentJars = new HashSet<String>();

	// 被哪些jar引用
	private Set<String> refrenceByJars = new HashSet<String>();

	// 找不到所属Jar包的依赖类，即为项目中的缺失类
	private Set<String> troubleClasses = new HashSet<String>();

	// 同一目录下所有jar之间的循环依赖关系
	private Set<String> cycleJars = new HashSet<String>();

	/**
	 * 判断一个jar包是否包含某个类
	 * 
	 * @param className
	 * @return
	 */
	public boolean hasThisClass(String className) {
		return ownedClasses.contains(className);
	}

	public void addOwnedPackages(String packageName) {
		ownedPackages.add(packageName);
	}

	public void addOwnedClasses(String className) {
		ownedClasses.add(className);
	}

	public void addDependentClasses(Set<String> classes) {
		dependentClasses.addAll(classes);
	}

	public void addDependentJars(String jarName) {
		dependentJars.add(jarName);
	}

	public void addTroubleClasses(String troubleClass) {
		troubleClasses.add(troubleClass);
	}

	public long getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(long buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String fileName) {
		int n = fileName.lastIndexOf("\\");
		if (n > -1) {
			this.name = fileName.substring(n + 1);
		} else {
			this.name = fileName;
		}
	}

	public Set<String> getOwnedPackages() {
		return ownedPackages;
	}

	public Set<String> getOwnedClasses() {
		return ownedClasses;
	}

	public Set<String> getDependentClasses() {
		dependentClasses.removeAll(ownedClasses);// 移除自身jar包的class
		return dependentClasses;
	}

	public Set<String> getDependentJars() {
		return dependentJars;
	}

	/**
	 * 获取jar中的缺失类
	 * 
	 * @return
	 */
	public Set<String> getTroubleClasses() {
		return troubleClasses;
	}

	public Set<String> getCycleJars() {
		return cycleJars;
	}

	public void setCycleJars(Set<String> cycleJars) {
		this.cycleJars = cycleJars;
	}

	public void addRefrenceByJars(String ref) {
		refrenceByJars.add(ref);
	}

	public Set<String> getRefrenceByJars() {
		return refrenceByJars;
	}

	public String toString() {
		return "[ name = " + name + ", buildVersion = " + buildVersion + " , ownedPackages = "
				+ ownedPackages.toString() + " ] ";
	}

}
