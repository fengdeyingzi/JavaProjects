import java.net.MalformedURLException;

import java.net.URL;

import javax.swing.JFileChooser;

import javax.swing.JFrame;

public class MediaTest {

	public static void main(String[] args) {
		JFileChooser fileChooser = new JFileChooser();

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			URL mediaURL = null;

			try {
				mediaURL = fileChooser.getSelectedFile().toURI().toURL();

			} catch (MalformedURLException e) {
				System.err.println("Can't create URL!");

			} // end try-catch

			if (mediaURL != null) {
				JFrame mediaTest = new JFrame("Media tester");

				mediaTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				MediaPanel mediaPanel = new MediaPanel(mediaURL);

				mediaTest.add(mediaPanel);

				mediaTest.setSize(300, 300);

				mediaTest.setVisible(true);

			} // end if: Find some URL

		} // end if

	}// end main

}// end class
