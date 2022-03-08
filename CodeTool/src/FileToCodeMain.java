import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileToCodeMain {
	
	public static void main(String[] args) {
		String action = "";
		String outtype = "c";
		byte[] textbuf = new byte[]{0x32,0x44};
		ArrayList<String> inputList = new ArrayList<String>();
		String output = "";
		String help = "这个小工具是将文件转换成代码，直接在项目里用代码文件\n    -i    输入的文件列表\n    -o    输出的文件\n    -h    查看帮助\n\n风的影子 制作";
		if(args.length == 0){
			System.out.println(help);
		}else{
			for(int i=0;i<args.length;i++){
				String item = args[i];
				if(item.startsWith("-")){
					action = item.substring(1);
				}else{
					if(action.equals("outtype")){
						outtype = item;
					}else if(action.equals("input") || action.equals("i")){
						System.out.println("添加文件："+item);
						inputList.add(item);
					}else if(action.equals("output") || action.equals("o")){
						output = item;
					}else if(action.equals("h")){
						
					}
				}
			}
			if(action.equals("h")){
				System.out.println(help);
			}else if(outtype.equals( "c")){
				String text = outCLang(inputList);
				System.out.println(text);
			}else if(outtype.equals("java")){
				String text = outJavaLang(inputList);
				System.out.println(text);
			}else if(outtype == "cpp"){
				
			}else if(outtype == "dart"){
				
			}else if(outtype == "js"){
				
			}
		}
	}
	
	
	public static String outCLang(ArrayList<String> inputList){
		StringBuffer buffer = new StringBuffer();
		buffer.append("#ifndef __FILE_DATA_H__\n");
		buffer.append("#define __FILE_DATA_H__\n\n");
		for(int i=0;i<inputList.size();i++){
			File file = new File(inputList.get(i));
			byte[] buf = new byte[(int) file.length()];
			String name = file.getName().toUpperCase().replace(".", "_");
			try {
				FileInputStream inputStream = new FileInputStream(file);
				inputStream.read(buf);
				inputStream.close();
				buffer.append(String.format("const char FILE_%s[%s] = {", name,buf.length));
				for(int n=0;n<buf.length;n++){
					int c = buf[n] & 0xff;
					if(c>=33 && c<=126 && c!='\''){
						buffer.append(String.format("'%c'", c));
					}
					else{
						buffer.append(String.format("0x%x", c));
					}
					if(n!=buf.length-1){
						buffer.append(", ");
					}
					if(n%32 == 0){
						buffer.append("\n");
					}
				}
				buffer.append("};\n\n");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		buffer.append("#endif\n");
		return buffer.toString();
	}
	
	public static String outJavaLang(ArrayList<String> inputList){
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		buffer.append("public class FileDataClass{\n\n");
		for(int i=0;i<inputList.size();i++){
			File file = new File(inputList.get(i));
			byte[] buf = new byte[(int) file.length()];
			String name = file.getName().toUpperCase().replace(".", "_");
			try {
				FileInputStream inputStream = new FileInputStream(file);
				inputStream.read(buf);
				inputStream.close();
				buffer.append(String.format("static byte[%d] FILE_%s = new byte[]{",buf.length, name));
				for(int n=0;n<buf.length;n++){
					int c = buf[n] & 0xff;
					
					buffer.append(String.format("0x%x", c));
					
					if(n!=buf.length-1){
						buffer.append(", ");
					}
					if(n%32 == 0){
						buffer.append("\n");
					}
				}
				buffer.append("};\n\n");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		buffer.append("}\n");
		return buffer.toString();
	}

}
