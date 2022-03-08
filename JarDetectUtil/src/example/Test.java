package example;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zz.check.CheckConfig;
import com.zz.check.JarChecker;
import com.zz.check.meta.JarInfo;
import com.zz.check.util.JarConflictDetectUtil;

public class Test {

	public static void main(String[] args) {
		CheckConfig checkConfig = new CheckConfig();
		List<JarInfo> lst = new JarChecker(checkConfig).check("D:/temp/lib1");
		for (JarInfo jar : lst) {
			System.out.println(jar);
			System.out.println(jar.getBuildVersion());// 编译时版本
			System.out.println(jar.getDependentClasses());
			System.out.println(jar.getDependentJars());
			System.out.println(jar.getCycleJars());// 循环依赖
			System.out.println(jar.getTroubleClasses());// 缺失的类
		}

		checkConflict(checkConfig);
	}

	// 检测类冲突
	public static void checkConflict(CheckConfig checkConfig) {
		long start = System.currentTimeMillis();
		String pathname = "D:\\temp\\lib1";
		Map<String, Set<String>> conflictMap = new JarConflictDetectUtil(checkConfig).getConflict(pathname);

		for (String jarName : conflictMap.keySet()) {
			System.out.println(jarName);
			System.out.println(conflictMap.get(jarName).toString());
		}

		System.out.println("jar解结束，累计耗时： " + (System.currentTimeMillis() - start) + " 毫秒");
	}

}
