
import java.awt.Color;

import org.jcodec.scale.AWTUtil;
import org.jcodec.scale.ColorUtil;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.jcodec.common.model.Picture;

public class PictureWindow extends JComponent{
Picture picture;

public PictureWindow(Picture pic) {
	this.picture = pic;
}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics2d = (Graphics2D)g;
		byte[][] data = picture.getData();
		int width = picture.getWidth();
		int height = picture.getHeight();
		
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		graphics2d.drawImage(bufferedImage, 0, 0, width, height, null);
//        try {
//			ImageIO.write(bufferedImage, "png", new File("test.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
}
