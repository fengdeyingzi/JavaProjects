package com.zz.check;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;

import com.zz.check.meta.ClassInfo;
import com.zz.check.meta.JarInfo;
import com.zz.check.util.JarCompileCheckUtil;

/**
 * jar文件检测类
 * 
 * @author zys
 *
 */
public class JarChecker {

	private List<JarInfo> jarInfoList = new ArrayList<JarInfo>();// 所有jar信息
	private ExecutorService executors = Executors.newFixedThreadPool(5);// 一般5个线程就可以了
	private final AtomicInteger completeCount = new AtomicInteger(0);

	CheckConfig checkConfig;// 检测配置

	public JarChecker(CheckConfig checkConfig) {
		this.checkConfig = checkConfig;
	}

	/**
	 * linkedlist记录遍历图时的所有过程路径信息,仅为方便查找循环依赖
	 */
	private LinkedList<String> linkedList = new LinkedList<String>();

	/**
	 * 对指定目录下的jar文件进行检测并返回相关结果信息
	 * 
	 * @param dirPath
	 * @return
	 */
	public List<JarInfo> check(String dirPath) {

		List<File> fileList = getJarFiles(dirPath);
		boolean useThread = fileList.size() > 10 ? true : false;
		for (File file : fileList) {
			JarInfo jarInfo = new JarInfo();
			// 多于10个jar包使用多线程解析
			if (useThread) {
				executors.execute(new AnalyzeOneJar(file, jarInfo, completeCount, checkConfig));
			} else {
				analyzeOneJar(file, jarInfo);
			}
			jarInfoList.add(jarInfo);
		}

		// 如果开启了多线程处理
		if (useThread) {
			executors.shutdown();
			while (true) {
				if (executors.isTerminated()) {
					break;
				}
			}
		}

		// 分析所有jar并为每个jar设置依赖和引用信息
		analyzeDependent();

		// 获得目录下所有jar包间的循环依赖关系并设置到每个JarInfo对象中
		Set<String> cycles = getAllCycles();
		for (JarInfo jar : jarInfoList) {
			jar.setCycleJars(cycles);
		}


		// completeCount比总量大1标记check过程全部结束
		completeCount.incrementAndGet();
		return jarInfoList;
	}

	public List<JarInfo> getCheckResult() {
		return this.jarInfoList;
	}

	/**
	 * 获得目录下的所有jar包
	 * @param dirPath 目录名称，多个目录以;隔开
	 * @return
	 */
	public List<File> getJarFiles(String dirPath) {
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
	 * 获取当前处理已经处理完成jar的数量
	 * 
	 * @return
	 */
	public int getCompleteCount() {
		return completeCount.get();
	}

	/**
	 * 获得目录下jar包的数量
	 * 
	 * @param dirPath
	 * @return
	 */
	public int getJarFileCount(String dirPath) {
		return getJarFiles(dirPath).size();
	}

	/**
	 * 获得目录下所有jar包之间的循环依赖
	 * 
	 * 解析方法说明： 1.将本次处理的jar包的依赖信息添加到linkedlist
	 * 2.从linkedlist取出所有以当前jar包名称结尾的路径，结合当前jar包的依赖信息拼接成新的路径并添加到linkedlist
	 * 3.遍历linkedlist，元素首尾名称相同则说明为循环依赖路径 例：jarA: A->B,A->C / jarB: B->C,B->E /
	 * jarC: C->A 遍历jarA后： A->B, A->C 遍历jarB后： A->B, A->C, B->C, B->E, A->B->C
	 * 遍历jarC后： A->B, A->C, B->C, B->E, A->B->C, C->A, A->C->A, B->C->A,
	 * A->B->C->A 循环路径为： A->C->A, A->B->C->A
	 * 
	 */
	private Set<String> getAllCycles() {
		Set<String> cycle = new HashSet<String>();
		List<String> lst;
		for (JarInfo jarInfo : jarInfoList) {

			// 将本次处理的jar包的依赖信息添加到linkedlist
			for (String dependJarName : jarInfo.getDependentJars()) {
				linkedList.add(jarInfo.getName() + "->" + dependJarName);
			}

			// 从linkedlist取出所有以当前jar包名称结尾的路径，结合当前jar包的依赖信息拼接成新的路径并添加到linkedlist
			lst = getEndWith(jarInfo.getName());
			for (String a : lst) {
				for (String dependJarName : jarInfo.getDependentJars()) {
					linkedList.add(a + "->" + dependJarName);
				}
			}
		}

		// 遍历linkedlist，元素首尾名称相同则说明为循环依赖路径
		String[] temp;
		for (String s : linkedList) {
			temp = s.split("->");
			if (temp[0].equals(temp[temp.length - 1])) {
				cycle.add(s);
			}
		}
		return cycle;
	}

	/**
	 * 获得Linkedlist中所有以指定名称结尾的路径名
	 * 
	 * @param name
	 * @return
	 */
	private List<String> getEndWith(String name) {
		List<String> list = new ArrayList<String>();
		String[] t;
		for (String s : linkedList) {
			t = s.split("->");
			if (t[t.length - 1].equals(name)) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * 分析所有jar并为每个jar设置依赖和引用信息
	 */
	private void analyzeDependent() {
		// 循环处理每一个jar
		for (JarInfo jarInfo : jarInfoList) {
			Set<String> depClasses = jarInfo.getDependentClasses();

			// 检测每个Jar的所有依赖类是否能找到归属
			for (String depClass : depClasses) {
				boolean flag = false;
				// 需要检测所有的jar包而不是找到就中断，因为可能多个jar中含有相同的类(即jar包类冲突情形)
				for (JarInfo in : jarInfoList) {
					if (in.getOwnedClasses().contains(depClass)) {
						if (!in.getName().equals(jarInfo.getName())) {
							jarInfo.addDependentJars(in.getName());
							in.addRefrenceByJars(jarInfo.getName());
						}
						flag = true;
					}
				}
				if (flag == false) {
					jarInfo.addTroubleClasses(depClass);
				}

			}
		}
	}

	/**
	 * 分析一个jar并为其设置相关信息
	 * 
	 * @param file
	 * @param jarInfo
	 */
	private void analyzeOneJar(File file, JarInfo jarInfo) {
		DependencyVisitor v = null;
		ZipFile zipeFile;
		try {
			ClassInfo classInfo = null;
			jarInfo.setBuildVersion(JarCompileCheckUtil.getBuildVersion(file));

			zipeFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> en = zipeFile.entries();
			InputStream in = null;
			while (en.hasMoreElements()) {
				ZipEntry entry = en.nextElement();
				String entryName = entry.getName();
				if (entryName.endsWith(".class")) {
					classInfo = new ClassInfo(entryName, zipeFile.getName());
					v = new DependencyVisitor(classInfo, checkConfig);
					in = zipeFile.getInputStream(entry);
					new ClassReader(in).accept(v, 0);

					// 设置jarinfo
					jarInfo.setName(zipeFile.getName());
					jarInfo.addOwnedClasses(classInfo.getName());
					jarInfo.addOwnedPackages(classInfo.getPackageName());
					jarInfo.addDependentClasses(classInfo.getDependentClasses());
					in.close();
				}
			}
			zipeFile.close();

		} catch (IOException e) {
		}
		// 完成数量
		completeCount.incrementAndGet();
	}

}
