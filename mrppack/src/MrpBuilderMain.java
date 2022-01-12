import java.io.File;
import java.util.ArrayList;

import com.xl.window.BuildWindow;

import mrpbuilder_java.MrpBuilder;
import mrpbuilder_java.MrpUnpack;
import mrpbuilder_java.MrpBuilder.Config;
/*
 * 控制台参数说明
 * -h 帮助
 * -
 */
public class MrpBuilderMain {
	public static void main(String[] args) {
		boolean useWindow = true;
		String type = "";
		String t = "pack";
		String displayname = "我的mrp";
		String filename = "mymrp.mrp";
		String vendor = "风的影子";
		String desc = "mrpbuilder生成 https://github.com/fengdeyingzi/JavaProjects";
		ArrayList<String> inputList = new ArrayList<String>();
		String output = "."+File.separator;
		String auth = "2f7cc7cde";
		String appid = "90001";
		String version = "1001";
		String scrw = "240";
		String scrh = "320";
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
				}else{
					if(type.equals("t") || type.equals("type")){
						t = item;
					}else if(type.equals("filename")){
						filename = item;
					}else if(type.equals("displayname")){
						displayname = item;
					}else if(type.equals("vendor")){
						vendor = item;
					}else if(type.equals("desc")){
						desc = item;
					}else if(type.equals("input") || type.equals("i")){
						inputList.add(item);
					}else if(type.equals("output")|| type.equals("o")){
						output = item;
					}else if(type.equals("auth")){
						auth = item;
					}else if(type.equals("appid")){
						appid = item;
					}else if(type.equals("version")){
						version = item;
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
						+"​    -t 类型 unpack解包 pack打包\n"
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
				
				ArrayList<File> list_file = new ArrayList<File>();
				MrpBuilder.Config config = new MrpBuilder().new Config();
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
					list_file.add(new File(inputList.get(i)));
				}
				MrpBuilder builder = new MrpBuilder();
				builder.pack(config, list_file);
			}
		}
		
	}
}
