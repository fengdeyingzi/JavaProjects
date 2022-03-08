import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
/*
APP图片精简工具
风的影子
*/
public class AssetsMain {
	
	public static void main(String[] args) {
		String code_path = "";
		String assets_path = "";
		StringBuilder builder = new StringBuilder();
		Scanner input = new Scanner(System.in);
		System.out.println("请输入代码路径");
		code_path = input.nextLine();
		
		//获取工程路径里所有代码
				Collection<File> list_file = FileUtils.listFiles(new File(code_path), ".java|.kt|.dart|.swift|.c|.h|.cpp|.js|.wxss|.wxml|.json|.php|.jsp|.asp|.go|.py".split("\\|"),true);
				for(File file:list_file){
					try {
						builder.append(FileUtils.read(file, "UTF-8"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String codeString = builder.toString();
				//统计字符数
				System.out.println("字符数："+codeString.length());
				//统计代码行数
				int lineSize = 1;
				for(int i=0;i<codeString.length();i++){
					if(codeString.charAt(i)=='\n'){
						lineSize++;
					}
				}
				System.out.println("代码行数："+lineSize);
		
		
		System.out.println("请输入assets路径");
		assets_path = input.nextLine();
		
		
		//列出所有图片
		Collection<File> list_imgfile = FileUtils.listFiles(new File(assets_path),".jpg|.JPG|.png|.PNG|.gif|.GIF|.bmp|.BMP".split("\\|"),true);
		for(File file:list_imgfile){
			String name = file.getName();
			if(codeString.indexOf(name)==-1){
				System.out.println("找不到图片"+file.getName());
			}
		}
		
		
		
	}

}
