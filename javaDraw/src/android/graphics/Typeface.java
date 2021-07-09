package android.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Typeface {

	//从assets资源中加载字体
//	Typeface createFromAsset(AssetManager mgr, String path){
//		
//	}
	Font mFont;
	
	private  Typeface() {
		
	}
	


	//通过路径加载字体文件
	public static Typeface createFromFile(String path){
		Typeface typeface = new Typeface();
		typeface.mFont = new Font(path, Font.PLAIN, 16);
		return typeface;
	}
	
	public static Typeface createFromName(String name) {
		Typeface typeface = new Typeface();
		typeface.mFont = new Font(name, Font.PLAIN, 16);
		return typeface;
	}
	
	public static Typeface createFromClass(String path){
		InputStream is = Typeface.class.getResourceAsStream(path);
		Typeface typeface = new Typeface();
		try {
			typeface.mFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typeface;
	}

	//通过指定文件加载字体
	public static Typeface createFromFile(File file){
		Typeface typeface = new Typeface();
		try {
//			FileInputStream is =new FileInputStream(file);
//			InputStream bis = new BufferedInputStream(is);
			try {
				typeface.mFont =  Font.createFont(Font.TRUETYPE_FONT,file);
//				typeface.mFont = new Font(typeface.mFont.getName(), typeface.mFont.getStyle(), 14);
				typeface.mFont.deriveFont(14);
			} catch (FontFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typeface;
	}

//	作者：英勇青铜5
//	链接：https://www.jianshu.com/p/1728b725b4a6
//	来源：简书
//	著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
}
