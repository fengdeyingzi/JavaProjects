import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.ForkJoinPool;

import com.xl.util.FileUtils;
import com.xl.window.BuildWindow;

import mrpbuilder_java.MrpBuilder;
import mrpbuilder_java.MrpInfo;
import mrpbuilder_java.MrpUnpack;
import mrpbuilder_java.MrpBuilder.Config;
/*
 * 控制台参数说明
 * -h 帮助
 * -
 */
public class MrpBuilderMain {
	static long startTime = 0;
	public static void main(String[] args) {
        
		boolean useWindow = false;
		String type = "";
		String t = "pack";
		String displayname = "我的mrp";
		String filename = "mymrp.mrp";
		String vendor = "风的影子";
		String desc = "mrpbuilder生成 https://github.com/fengdeyingzi/JavaProjects";
		ArrayList<String> inputList = new ArrayList<String>();
		ArrayList<String> includeList = new ArrayList<String>();
		ArrayList<String> defineList = new ArrayList<String>();
		String output = "."+File.separator;
		String auth = "2f7cc7cde";
		String appid = "90001";
		String version = "1001";
		String scrw = "240";
		String scrh = "320";
		boolean useGZIP = false;
		boolean useBMP565 = false;
		startTime = System.currentTimeMillis();
		MrpBuilder.Config config = new MrpBuilder().new Config();
		if(args.length == 0){
			if(useWindow){
				BuildWindow window = new BuildWindow();
				window.setVisible(true);
			}else{
				System.out.println("请输入命令");
			}
					
		}else{
			for(int i=0;i<args.length;i++){
				String item = args[i];
				if(item.startsWith("-")){
					type = item.substring(1);
					if(item.startsWith("-I")){
						includeList.add(item);
					}else if(item.equals("-gzip")){
						useGZIP = true;
					}else if(item.equals("-bmp565")){
						useBMP565 = true;
					}else if(item.startsWith("-D")){
						defineList.add(item);
					}
				}else{
					if(type.equals("t") || type.equals("type")){
						t = item;
					}else if(type.equals("filename")){
						filename = item;
						config.FileName = item;
					}else if(type.equals("displayname")){
						displayname = item;
						config.DisplayName = item;
					}else if(type.equals("vendor")){
						vendor = item;
						config.Vendor = item;
					}else if(type.equals("desc")){
						desc = item;
						config.Desc = item;
					}else if(type.equals("input") || type.equals("i")){
//						inputList.add(item);
						File file = new File(item);
						if(file.isDirectory()){
							File[] lists = file.listFiles();
							for(File itemfile:lists){
								if(itemfile.isFile()){
									inputList.add(itemfile.getPath());
								}
							}
						}else {
							inputList.add(item);
						}
					}else if(type.equals("output")|| type.equals("o")){
						output = item;
					}else if(type.equals("auth")){
						auth = item;
					}else if(type.equals("appid")){
						appid = item;
						config.Appid =  Integer.valueOf(item);
					}else if(type.equals("version")){
						version = item;
						config.Version = Integer.valueOf(item);
					}else if(type.equals("scrw")){
						scrw = item;
					}else if(type.equals("scrh")){
						scrh = item;
					}
				}
				
			}
			if(type.equals("h")){
				System.out.println("​    -h 帮助\n"
						+"\n"
						+"​    -filename 文件名\n"
						+"\n"
						+"​    -displayname 显示名\n"
						+"\n"
						+"​    -vendor 作者\n"
						+"\n"
						+"​    -desc 详情\n"
						+"\n"
						+"​    -t 类型 unpack 解包;pack 打包;info 获取mrp信息;setinfo 设置mrp信息;build 编译\n"
						+"\n"
						+"​    -i 输入文件（可指定文件夹）\n"
						+"\n"
						+"​    -o 输出文件\n"
						+"\n"
						+"​    -auth 编译器授权字符串 2f7cc7cde\n"
						+"\n"
						+"​    -appid 应用id\n"
						+"\n"
						+"​    -version 版本号\n"
						+"\n"
						+"​    -scrw 屏幕宽度\n"
						+"\n"
						+"​    -scrh 屏幕高度");
				System.out.println("风的影子 制作");
				System.out.println("当前运行在 "+System.getProperty("java.vm.specification.name"));
			}
			else if(t.equals("unpack")){ //解包
				if(inputList.size()>0){
					MrpUnpack unpack = new MrpUnpack(new File(inputList.get(0)));
				    unpack.unpack(output);
				}else{
					System.out.println("请输入mrp文件");
				}
				
			}else if(t.equals("pack")){ //打包
				if(inputList.size()==0){
					System.out.println("请输入文件");
				}
				
				ArrayList<String> list_file = new ArrayList<String>();
				config = new MrpBuilder().new Config();
				config.FileName = filename;
				config.DisplayName = displayname;
				config.Vendor = vendor;
				config.Desc = desc;
				config.path = output;
				config.AuthStr = auth;
				config.Appid = Integer.valueOf(appid);
				config.Version = Integer.valueOf(version);
				config.ScreenWidth = Integer.valueOf(scrw);
				config.ScreenHeight = Integer.valueOf(scrh);
				config.list_file = new ArrayList<MrpBuilder.FileItem>();
				for(int i=0;i<inputList.size();i++){
					File item = new File(inputList.get(i));
					if(item.isDirectory()){
						File[] lists = item.listFiles();
						for(File itemfile:lists){
							if(itemfile.isFile()){
								list_file.add(itemfile.getPath());
							}
						}
					}else{
						list_file.add(inputList.get(i));
					}
					
				}
				MrpBuilder builder = new MrpBuilder();
				builder.setGZIP(useGZIP);
				builder.pack(config, list_file);
			}
			else if(t.equals("info")){ //获取mrp信息
				if(inputList.size()==0){
					System.out.println("请添加输入文件");
					return;
				}
				MrpInfo mrpInfo = new MrpInfo(inputList.get(0));
				config = mrpInfo.getInfo();
				System.out.println("应用名："+config.DisplayName);
				System.out.println("文件名："+config.FileName);
				System.out.println("作者："+config.Vendor);
				System.out.println("Appid："+config.Appid);
				System.out.println("版本："+config.Version);
				System.out.println("描述："+config.Desc);
				System.out.println("文件列表：");
				for(int i=0;i<config.list_file.size();i++){
					System.out.println(String.format("    -> %s      offset:%d,size:%d", config.list_file.get(i).filename, config.list_file.get(i).offset, config.list_file.get(i).len ));
				}
				
			}else if(t.equals("setinfo")){ //设置mrp信息
				if(inputList.size()==0){
					System.out.println("请添加输入文件");
					return;
				}
				MrpInfo mrpInfo = new MrpInfo(inputList.get(0));
				mrpInfo.setInfo(config);
			}
			else if(t.equals("build")){
				if(inputList.size()==0){
					System.out.println("请添加输入文件");
					return;
				}
				if(!testCompileMRP2(inputList,includeList,defineList)){
					return;
				}
				
				if(!inputList.contains("cfunction.ext")){
					inputList.add(getTempPath()+File.separator+"cfunction.ext");
				}
				if(!inputList.contains("start.mr")){
					//判断start.mr是否存在
					File startFile = new File(getTempPath()+File.separator, "start.mr");
					if(!startFile.isFile()){
						String startmr = "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";
						OutputStream outputStream;
						try {
						outputStream = new FileOutputStream(startFile);
						outputStream.write(Base64.getDecoder().decode(startmr));
						outputStream.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					inputList.add(startFile.getAbsolutePath());
				}
				ArrayList<String> list_file = new ArrayList<String>();
				config = new MrpBuilder().new Config();
				config.FileName = filename;
				config.DisplayName = displayname;
				config.Vendor = vendor;
				config.Desc = desc;
				config.path = output;
				config.AuthStr = auth;
				config.Appid = Integer.valueOf(appid);
				config.Version = Integer.valueOf(version);
				config.ScreenWidth = Integer.valueOf(scrw);
				config.ScreenHeight = Integer.valueOf(scrh);
				config.list_file = new ArrayList<MrpBuilder.FileItem>();
				for(int i=0;i<inputList.size();i++){
					File item = new File(inputList.get(i));
					if(item.isDirectory()){
						File[] lists = item.listFiles();
						for(File itemfile:lists){
							if(itemfile.isFile()){
								list_file.add(itemfile.getPath());
							}
						}
					}else {
						list_file.add(inputList.get(i));
					}
					
				}
				MrpBuilder builder = new MrpBuilder();
				
				System.out.println("--> 开始打包");
				//打包之前移除掉c和h文件
				for(int i=list_file.size()-1;i>=0;i--){
					String item = list_file.get(i);
					String endname = FileUtils.getEndName(list_file.get(i)).toLowerCase();
					if(endname.equals(".s") || endname.equals(".h") || endname.equals(".c") || endname.equals(".cpp") || endname.equals(".hpp")){
						list_file.remove(i);
					}
				}
				builder.setGZIP(useGZIP);
				builder.setBMP565(useBMP565);
				builder.pack(config, list_file);
				System.out.println("耗时："+(((float)(System.currentTimeMillis()-startTime))/1000)+" s");
			}
		}
		
	}
	
	public static String getTempName(String dir,String name){
		String endName = FileUtils.getEndName(name);
		String tempName = dir + File.separator + name.substring(0, name.length()-endName.length())+".o";
		String tempDir = FileUtils.getDir(tempName);
		File tempFile = new File(tempDir);
		if(!tempFile.isDirectory()){
			tempFile.mkdirs();
		}
//		String tempPath = System.getProperty("java.io.tmpdir");
		return tempName;
	}
	
	public static boolean testCMD(String cmd){
		boolean isTRUE = true;
		
		try {
			
			Process p = Runtime.getRuntime().exec(cmd);
			p.getErrorStream();
			final BufferedReader input_err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String line = null;
					try {
						while ((line = input_err.readLine()) != null) {
							System.out.println("ERR:"+line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
			br.close();
			input_err.close();
			if(p.exitValue() != 0){
				isTRUE = false;
			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			
		}
		return isTRUE;
	}
	
	public static void mrpeg(String inputpath,String outputpath){
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(inputpath));
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
			OutputStream outputStream = new FileOutputStream(new File(outputpath));
			outputStream.write("MRPGCMAP".getBytes());
			outputStream.write(data);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static String getTempPath(){
		String tempPath = System.getProperty("java.io.tmpdir");
		String dir = "2";
		tempPath = tempPath + File.separator + dir;
		return tempPath;
	}
	
	public static boolean testCompileMRP2(ArrayList<String> list_file, ArrayList<String> includeList, ArrayList<String> defineList){
		SecurityManager m = new SecurityManager();
		String tempPath = System.getProperty("java.io.tmpdir");
		String dir = "2";
		tempPath = tempPath + File.separator + dir;
		File tempDir = new File(tempPath);
		if(!tempDir.isDirectory()){
			tempDir.mkdir();
		}
		String MRPBUILDER = "C:\\skysdk\\compiler";
		try{
		
			String builderc = System.getenv("MRPBUILDER");
			if(builderc!=null){
				MRPBUILDER = builderc;
			}else{
				System.out.println("未找到 MRPBUILDER 环境变量，请在环境变量中设置");
			}
		}catch(Exception e){
			
		}
		System.out.println("mrpbuilder路径："+MRPBUILDER);
		
		
		
		
		StringBuffer buffer_link = new StringBuffer();
				
		buffer_link.append(		"armlink -ropi -rwpi -ro-base 0x80000 -remove -first mr_c_function_load -entry mr_c_function_load -o "+tempPath+File.separator+"mr_cfunction.fmt ");
		for(int i=0;i<list_file.size();i++){
			String endname = FileUtils.getEndName(list_file.get(i)).toLowerCase();
			if(endname.equals(".s") || endname.equals(".h") || endname.equals(".c") || endname.equals(".cpp") || endname.equals(".hpp")){
			buffer_link.append(getTempName(tempPath,list_file.get(i))+" ");
			}
			
		}
		
		buffer_link.append(String.format("%s\\mr_helper.lib(mr_helper.o) %s\\mr_helper.lib %s\\mr_helperexb.lib ", MRPBUILDER,MRPBUILDER,MRPBUILDER));
		System.out.println("--> 调用armcc编译");
		
		for(int i=0;i<list_file.size();i++){
			StringBuffer buffer_armcc = new StringBuffer();
			buffer_armcc.append(String.format("armcc -c -I. -c -O2 -Otime -DGET_C_FUNCTION_P()=(*(((mr_c_function_st**)mr_c_function_load)-1)) -DGET_HELPER()=(*(((mr_table**)mr_c_function_load)-2)) -I%s\\..\\include -I%s\\..\\include\\plugins -cpu ARM7EJ-S -littleend -apcs /ropi/rwpi/interwork -fa -zo ", MRPBUILDER,MRPBUILDER));
			for(int j=0;j<includeList.size();j++){
				buffer_armcc.append(includeList.get(j)+" ");
			}
			for(int j=0;j<defineList.size();j++){
				buffer_armcc.append(defineList.get(j)+" ");
			}
			buffer_armcc.append("-o ");
			String endname = FileUtils.getEndName(list_file.get(i)).toLowerCase();
			if(endname.equals(".s") || endname.equals(".h") || endname.equals(".c") || endname.equals(".cpp") || endname.equals(".hpp")){
				buffer_armcc.append(getTempName(tempPath,list_file.get(i))+" ");
				buffer_armcc.append(""+list_file.get(i)+" ");
//				System.out.println(buffer_armcc.toString());
				System.out.println("Compile ... "+list_file.get(i));
				if(!testCMD(buffer_armcc.toString())){
					return false;
				}
			}
		}
		
		
		
		System.out.println("--> 调用armlink");
		System.out.println(buffer_link.toString());
		if(!testCMD(buffer_link.toString())){
			return false;
		}
		System.out.println("--> 调用fromelf");
		if(!testCMD(String.format("fromelf -bin -o %smr_cfunction.ext %smr_cfunction.fmt", tempPath+File.separator,tempPath+File.separator))){
			return false;
		}
		System.out.println("--> 生成ext");
		mrpeg(tempPath+File.separator+"mr_cfunction.ext", tempPath+File.separator+"cfunction.ext");
		return true;

	}
	
	public static boolean testCompileMRP(ArrayList<String> list_file, ArrayList<String> includeList){
		SecurityManager m = new SecurityManager();
		String tempPath = System.getProperty("java.io.tmpdir");
		String dir = "2";
		tempPath = tempPath + File.separator + dir;
		File tempDir = new File(tempPath);
		if(!tempDir.isDirectory()){
			tempDir.mkdir();
		}
		String MRPBUILDER = "C:\\skysdk\\compiler";
		try{
		
			String builderc = System.getenv("MRPBUILDER");
			if(builderc!=null){
				MRPBUILDER = builderc;
			}else{
				System.out.println("未找到 MRPBUILDER 环境变量，请在环境变量中设置");
			}
		}catch(Exception e){
			
		}
		System.out.println("mrpbuilder路径："+MRPBUILDER);
		
		
		
		StringBuffer buffer_armcc = new StringBuffer();
		buffer_armcc.append(String.format("armcc -c -I. -c -O2 -Otime -DGET_C_FUNCTION_P()=(*(((mr_c_function_st**)mr_c_function_load)-1)) -DGET_HELPER()=(*(((mr_table**)mr_c_function_load)-2)) -I%s\\..\\include -I%s\\..\\include\\plugins -cpu ARM7EJ-S -littleend -apcs /ropi/rwpi/interwork -fa -zo ", MRPBUILDER,MRPBUILDER));
		for(int i=0;i<includeList.size();i++){
			buffer_armcc.append(includeList.get(i)+" ");
		}
		buffer_armcc.append("-o ");
		for(int i=0;i<list_file.size();i++){
			String endname = FileUtils.getEndName(list_file.get(i)).toLowerCase();
			if(endname.equals(".s") || endname.equals(".h") || endname.equals(".c") || endname.equals(".cpp") || endname.equals(".hpp")){
				buffer_armcc.append(getTempName(tempPath,list_file.get(i))+" ");
				buffer_armcc.append(""+list_file.get(i)+" ");
			}
		}
		StringBuffer buffer_link = new StringBuffer();
				
		buffer_link.append(		"armlink -ropi -rwpi -ro-base 0x80000 -remove -first mr_c_function_load -entry mr_c_function_load -o "+tempPath+File.separator+"mr_cfunction.fmt ");
		for(int i=0;i<list_file.size();i++){
			String endname = FileUtils.getEndName(list_file.get(i)).toLowerCase();
			if(endname.equals(".s") || endname.equals(".h") || endname.equals(".c") || endname.equals(".cpp") || endname.equals(".hpp")){
			buffer_link.append(getTempName(tempPath,list_file.get(i))+" ");
			}
			
		}
		
		buffer_link.append(String.format("%s\\mr_helper.lib(mr_helper.o) %s\\mr_helper.lib %s\\mr_helperexb.lib ", MRPBUILDER,MRPBUILDER,MRPBUILDER));
		System.out.println("--> 调用armcc编译");
		System.out.println(buffer_armcc.toString());
		if(!testCMD(buffer_armcc.toString())){
			return false;
		}
		System.out.println("--> 调用armlink");
		System.out.println(buffer_link.toString());
		if(!testCMD(buffer_link.toString())){
			return false;
		}
		System.out.println("--> 调用fromelf");
		if(!testCMD(String.format("fromelf -bin -o %smr_cfunction.ext %smr_cfunction.fmt", tempPath+File.separator,tempPath+File.separator))){
			return false;
		}
		System.out.println("--> 生成ext");
		mrpeg(tempPath+File.separator+"mr_cfunction.ext",tempPath+File.separator+"cfunction.ext");
		return true;

	}
}
