package com.zz.check;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;

import com.zz.check.meta.ClassInfo;
import com.zz.check.meta.JarInfo;
import com.zz.check.util.JarCompileCheckUtil;

/**
 * jar文件解析
 * 
 * @author zys
 *
 */
public class AnalyzeOneJar implements Runnable {

	private File file;
	private JarInfo jarInfo;
	private AtomicInteger completeCount;
	private CheckConfig checkConfig;

	public AnalyzeOneJar(File file, JarInfo jarInfo, AtomicInteger completeCount, CheckConfig checkConfig) {
		this.file = file;
		this.jarInfo = jarInfo;
		this.completeCount = completeCount;
		this.checkConfig = checkConfig;
	}

	public void run() {
		analyzeOneJar();
		completeCount.incrementAndGet();
	}

	/**
	 * 分析一个jar并为其设置相关信息
	 * 
	 * @param filePath
	 * @return
	 */
	private void analyzeOneJar() {
		DependencyVisitor v = null;
		ZipFile zipeFile;
		try {
			ClassInfo classInfo = null;
			jarInfo.setBuildVersion(JarCompileCheckUtil.getBuildVersion(file));

			zipeFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> en = zipeFile.entries();
			InputStream in = null;
			while (en.hasMoreElements()) {
				try{
					
				
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			zipeFile.close();

		} catch (IOException e) {
		}
	}
}
