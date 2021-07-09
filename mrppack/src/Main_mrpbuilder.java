import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import mrpbuilder_java.*;

public class Main_mrpbuilder {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test2(String[] args){
		MrpBuilder builder = new MrpBuilder();
		System.out.println(""+args.length);
		try {
			builder.main(args);
			System.out.println("打包成功");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test(){
		ArrayList<File> list_file = new ArrayList<File>();
		MrpBuilder.Config config = new MrpBuilder().new Config();
		config.FileName = "测试";
		config.DisplayName = "test";
		config.Vendor = "风的影子";
		config.Desc = "测试打包";
		config.path = "D:\\Go\\output.mrp";
		config.AuthStr = "";
		config.list_file = new ArrayList<MrpBuilder.FileItem>();
		list_file.add(new File("D:\\Go\\android.mk"));
		
		MrpBuilder builder = new MrpBuilder();
		builder.pack(config, list_file);
		
	}
}
