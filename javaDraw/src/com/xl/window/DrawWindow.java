package com.xl.window;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xl.util.ImageUtil;
import com.xl.view.CirColorView;
import com.xl.view.DrawView;
import com.xl.view.TestView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class DrawWindow extends JFrame{
	DrawView drawView;
	Activity activity;
	
	public DrawWindow() {
		// TODO Auto-generated constructor stub
		setTitle("绘图练习");
		File file = new File("");
		try {
			System.out.println(""+file.getCanonicalPath());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		activity = new Activity(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);
		JPanel mainJPanel= new JPanel();
		setContentPane(mainJPanel);
		setLayout(new BoxLayout(mainJPanel, BoxLayout.Y_AXIS));
		drawView= new DrawView();
		drawView.setPreferredSize(new Dimension(320, 60));
		
		
		getContentPane().add(drawView);
		TestView testView = new TestView(activity);
		
		CirColorView colorView = new CirColorView(activity);
		colorView.setColor(0x8050f050);
		colorView.setCirSize(4);
		colorView.setPadding(8);
//		colorView.setScrollX(50);
//		colorView.setScrollY(30);
//		colorView.setCirColor(0x8060a0f0);
		colorView.setPreferredSize(new Dimension(128, 128));
		colorView.setMaximumSize(new Dimension(128, 128));
		getContentPane().add(colorView);
		getContentPane().add(testView);
		Toast.makeText(activity, "测试toast").display();
		
		Bitmap bitmap = BitmapFactory.decodeFile("D:\\1.png");
		ImageUtil.zoomBitmap(bitmap, 1024,2048, "D:\\11.png");
		
	}

}
