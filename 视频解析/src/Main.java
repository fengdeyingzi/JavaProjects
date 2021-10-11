

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;

public class Main {
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		int frameNumber = 140;
		Picture picture;
		try {
			picture = FrameGrab.getFrameFromFile(new File("D:\\Videos\\PR剪辑教程！6分钟学会剪辑！~1.mp4"), frameNumber);
			byte[][] data = picture.getData();
			for(int i=0;i<data.length;i++){
				byte[] item = data[i];
//				for(int j=0;j<item.length;j++){
//					System.out.print(String.format("%x ", item[j]));
//				}
				System.out.println(""+data.length+" "+data[0].length+" "+picture.getWidth()+" "+picture.getHeight()+"-\n");
			}
			JFrame jFrame = new JFrame();
			jFrame.setSize(new Dimension(1920,1080));
			PictureWindow view = new PictureWindow(picture);
			
			jFrame.setContentPane(view);
			view.repaint();
			jFrame.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JCodecException e) {
			e.printStackTrace();
		}
//		ImageIO.write(frame, "png", new File("frame_150.png"));
		
	}
}
