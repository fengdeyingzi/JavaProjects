package com.xl.window;

import java.awt.BorderLayout;

import com.xl.game.tool.CopyFile;
import com.xl.game.tool.FileUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.Box;
import java.awt.Component;

public class GradleReplace extends JFrame {

	private JPanel contentPane;
	private JTextField input;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					GradleReplace frame = new GradleReplace();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the frame.
	 */
	public GradleReplace() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("gradle项目版本转换 - 风的影子");
		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_1);
		
		JLabel text_dir = new JLabel("项目文件夹");
		horizontalBox_1.add(text_dir);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue);
		
		Box horizontalBox = Box.createHorizontalBox();
		contentPane.add(horizontalBox);
		
		input = new JTextField();
		horizontalBox.add(input);
		input.setMaximumSize(new Dimension(1024, 21));
		input.setToolTipText("选择文件夹");
		input.setColumns(10);
		
		JButton btn_select = new JButton("选择");
		horizontalBox.add(btn_select);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_2);
		
		JButton btn_con = new JButton("开始转换");
		btn_con.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String project_path = input.getText();
				File file = new File(project_path);
				
				//修改 build.gradle 中 com.android.tools.build:gradle 字符串
				String temp;
				try {
					temp = FileUtils.read(new File(file,"build.gradle"), "UTF-8");
				
				int index = temp.indexOf("\'com.android.tools.build:gradle");
				if(index<0) index = temp.indexOf("\"com.android.tools.build:gradle");
				StringBuffer buffer = new StringBuffer();
				if(index>0){
					buffer.append(temp.substring(0,index)); //不包含引号
					String text_temp = temp.substring(index+1);
					int endIndex = text_temp.indexOf("\'");
					
					buffer.append("\'com.android.tools.build:gradle:3.5.1\'");
					buffer.append(text_temp.substring(endIndex+1));
					FileUtils.writeText(new File(file,"build.gradle").getPath(), buffer.toString(), "UTF-8");
					System.out.println(buffer.toString());
				}
				else{
					System.out.println("搜索字符串com.android.tools.build:gradle失败");
				}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//将gradle文件夹整个替换
				if(file.isDirectory()){
					CopyFile.copyFolder("gradle", file.getPath()+ file.separator+ "gradle");
				}
			}
		});
		horizontalBox_2.add(btn_con);
		
		Box horizontalBox_3 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_3);
		
		JLabel lblNewLabel = new JLabel("本工具转换gradle的版本");
		horizontalBox_3.add(lblNewLabel);
		btn_select.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
	}

}
