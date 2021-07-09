package android.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class BitmapFactory {

	
	 /**
     * Decode a file path into a bitmap. If the specified file name is null,
     * or cannot be decoded into a bitmap, the function returns null.
     *
     * @param pathName complete path name for the file to be decoded.
     * @return the resulting decoded bitmap, or null if it could not be decoded.
     */
	 public static Bitmap decodeFile(String pathName) {
		
	     BufferedImage image = null;
		try {
			FileInputStream fis = new FileInputStream(new File(pathName));
			image = ImageIO.read(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     Bitmap bitmap = new Bitmap(image);
	     return bitmap;
	    }
	 
	   public static Bitmap decodeStream(InputStream is) {
		   BufferedImage image = null;
			try {
				
				image = ImageIO.read(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		     Bitmap bitmap = new Bitmap(image);
		     return bitmap;
	    }
}
