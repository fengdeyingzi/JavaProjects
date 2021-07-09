
import java.awt.BorderLayout;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.media.ControllerEvent;

import javax.media.ControllerListener;

import javax.media.Manager;

import javax.media.MediaLocator;

import javax.media.Player;

import javax.swing.JFrame;

import javax.swing.JPanel;

public class MediaTest2 extends JFrame {
	private static final long serialVersionUID = -9100641419343097372L;

	private Player player;

	private MediaTest2() {
		JPanel panel = (JPanel) this.getContentPane();

		panel.setLayout(new BorderLayout());

		String mediaFile = "file:///D:\\Videos\\浏览Github必备的5款神器级别的Chrome插件~2.avi";

		try {
			// 确定视频文件的位置

			MediaLocator mlr = new MediaLocator(mediaFile);

			// 创建一个播放器对象

			this.player = Manager.createRealizedPlayer(mlr);

			this.player.addControllerListener(new ControllerListener() {
				public void controllerUpdate(ControllerEvent event) {
					if (event instanceof javax.media.RealizeCompleteEvent) {
					}

				}

			});

			if (this.player.getVisualComponent() != null) {
				panel.add("Center", player.getVisualComponent());

			}

			if (this.player.getControlPanelComponent() != null) {
				panel.add("South", player.getControlPanelComponent());

			}

		} catch (Exception e) {
			System.err.println("Got exception " + e);

		}

		this.addWindowListener(new WindowAdapter() {
			@Override

			public void windowClosing(WindowEvent e) {
				player.stop();

				player.deallocate();

				player.close();

				dispose();

			}

			@Override

			public void windowClosed(WindowEvent e) {
				System.exit(0);

			}

		});

		this.setSize(320, 260);

	}

	/**
	 * 
	 * @param args
	 * 
	 */

	public static void main(String[] args) {
		MediaTest2 frame = new MediaTest2();

		frame.setVisible(true);

	}

}