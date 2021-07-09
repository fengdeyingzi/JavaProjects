package android.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class Bitmap {
	
	public BufferedImage image;
	public Bitmap (BufferedImage image) {
		this.image = image;
	}
	
	 public static Bitmap createBitmap(int width, int height,  Config config) {
		 Bitmap bitmap = new Bitmap(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
	     return bitmap;
	 }
	 
	 public boolean compress(CompressFormat format, int quality, OutputStream stream) {
		//首先创建一个BufferedImage变量，因为ImageIO写图片用到了BufferedImage变量。
	        BufferedImage bi = this.image;
	        FileOutputStream stream2;
	        if(stream instanceof FileOutputStream){
	        	stream2 = ((FileOutputStream)stream);
	        }
	        switch (format) {
			case JPEG:
				try {
					ImageIO.write(bi,"jpg",stream);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				break;
			case PNG:
				try {
					ImageIO.write(bi,"png",stream);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
	        return false;
	    }
    
    
    public int getWidth(){
    	return image.getWidth();
    }
    
    public int getHeight() {
		return image.getHeight();
	}
    
    public int getPixel(int x, int y) {
        return 0xff000000 | image.getRGB(x, y);
    }
    
    public void setPixel(int x,int y,int color) {
		image.setRGB(x, y, color&0xffffff);
	}

    public enum Config {
        // these native values must match up with the enum in SkBitmap.h

        /**
         * Each pixel is stored as a single translucency (alpha) channel.
         * This is very useful to efficiently store masks for instance.
         * No color information is stored.
         * With this configuration, each pixel requires 1 byte of memory.
         */
        ALPHA_8     (BufferedImage.TYPE_INT_ARGB_PRE),

        /**
         * Each pixel is stored on 2 bytes and only the RGB channels are
         * encoded: red is stored with 5 bits of precision (32 possible
         * values), green is stored with 6 bits of precision (64 possible
         * values) and blue is stored with 5 bits of precision.
         *
         * This configuration can produce slight visual artifacts depending
         * on the configuration of the source. For instance, without
         * dithering, the result might show a greenish tint. To get better
         * results dithering should be applied.
         *
         * This configuration may be useful when using opaque bitmaps
         * that do not require high color fidelity.
         *
         * <p>Use this formula to pack into 16 bits:</p>
         * <pre class="prettyprint">
         * short color = (R & 0x1f) << 11 | (G & 0x3f) << 5 | (B & 0x1f);
         * </pre>
         */
        RGB_565     (BufferedImage.TYPE_USHORT_565_RGB),

        /**
         * Each pixel is stored on 2 bytes. The three RGB color channels
         * and the alpha channel (translucency) are stored with a 4 bits
         * precision (16 possible values.)
         *
         * This configuration is mostly useful if the application needs
         * to store translucency information but also needs to save
         * memory.
         *
         * It is recommended to use {@link #ARGB_8888} instead of this
         * configuration.
         *
         * Note: as of {@link android.os.Build.VERSION_CODES#KITKAT},
         * any bitmap created with this configuration will be created
         * using {@link #ARGB_8888} instead.
         *
         * @deprecated Because of the poor quality of this configuration,
         *             it is advised to use {@link #ARGB_8888} instead.
         */
        @Deprecated
        ARGB_4444   (BufferedImage.TYPE_4BYTE_ABGR),

        /**
         * Each pixel is stored on 4 bytes. Each channel (RGB and alpha
         * for translucency) is stored with 8 bits of precision (256
         * possible values.)
         *
         * This configuration is very flexible and offers the best
         * quality. It should be used whenever possible.
         *
         * <p>Use this formula to pack into 32 bits:</p>
         * <pre class="prettyprint">
         * int color = (A & 0xff) << 24 | (B & 0xff) << 16 | (G & 0xff) << 8 | (R & 0xff);
         * </pre>
         */
        ARGB_8888   (BufferedImage.TYPE_4BYTE_ABGR),

        /**
         * Each pixels is stored on 8 bytes. Each channel (RGB and alpha
         * for translucency) is stored as a
         * {@link android.util.Half half-precision floating point value}.
         *
         * This configuration is particularly suited for wide-gamut and
         * HDR content.
         *
         * <p>Use this formula to pack into 64 bits:</p>
         * <pre class="prettyprint">
         * long color = (A & 0xffff) << 48 | (B & 0xffff) << 32 | (G & 0xffff) << 16 | (R & 0xffff);
         * </pre>
         */
        RGBA_F16    (6),

        /**
         * Special configuration, when bitmap is stored only in graphic memory.
         * Bitmaps in this configuration are always immutable.
         *
         * It is optimal for cases, when the only operation with the bitmap is to draw it on a
         * screen.
         */
        HARDWARE    (7);

        final int nativeInt;

        private static Config sConfigs[] = {
            null, ALPHA_8, null, RGB_565, ARGB_4444, ARGB_8888, RGBA_F16, HARDWARE
        };

        Config(int ni) {
            this.nativeInt = ni;
        }

        static Config nativeToConfig(int ni) {
            return sConfigs[ni];
        }
    }
    
    public enum CompressFormat {
        JPEG    (0),
        PNG     (1),
        WEBP    (2);

        CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
}
