package com.xl.window;


import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONObject;

import com.xl.util.ClipBoard;
import com.xl.util.CodeUtil;
import com.xl.util.JsonFormat;
import com.xl.util.JsonToCode;
import com.xl.util.MarkDownToCode;
import com.xl.util.ParamToCode;
import com.xl.util.ParamTojson;
import com.xl.util.ParameterToCode;
import com.xl.util.XmlToJson;

public class JSONToCodeWindow extends JFrame{

	JTextArea editArea;
	JTextField textField;
	JButton button;
	JButton button2;
	JButton button_format;
	JButton button_xmlTojson;
	JButton button_paramToJson;
	JButton button_iToCode; //企查查接口转代码
	JButton button_mdToCode;
	JButton button_codestatistics;
	JButton button_toflutter;
	JButton button_tojava;
	JScrollPane scrollPane;
	TextWindow textWindow;
	public JSONToCodeWindow(){
		int screen_w,screen_h;
		textWindow=  new TextWindow();
		int array[]= new int[]{1,21};
		System.out.println("数组"+array.toString());
		
		Toolkit toolkit= Toolkit.getDefaultToolkit();
		screen_w= (int) toolkit.getScreenSize().getWidth();
		screen_h = (int) toolkit.getScreenSize().getHeight();
		JPanel mainJPanel= new JPanel();
		setContentPane(mainJPanel);
		setLayout(new BoxLayout(mainJPanel, BoxLayout.Y_AXIS));
		Box box_v= Box.createVerticalBox();
		getContentPane().add(box_v);
		mainJPanel.setSize(640, 480);
		//
		
		 editArea= new JTextArea();
		 editArea.setColumns(20);
		 editArea.setRows(10);
		 textField= new JTextField();
		 scrollPane= new JScrollPane(editArea);
		 //scrollPane.add(editArea);
		 
		 button= new JButton("json转代码");
		//设置对齐方式 不然会出问题
		button.setAlignmentX((float) 0.5);
		
		button2= new JButton("参数转代码");
		button2.setAlignmentX(0.5f);
		
		button_xmlTojson = new JButton("xml转json");
		button_xmlTojson.setAlignmentX(0.5f);
		
		button_format= new JButton("json格式化");
		button_format.setAlignmentX(0.5f);
		
		button_paramToJson = new JButton("参数转json");
		button_paramToJson.setAlignmentX(0.5f);
		
		button_iToCode = new JButton("接口转代码");
		button_iToCode.setAlignmentX(0.5f);
		
		button_mdToCode = new JButton("md转代码");
		button_mdToCode.setAlignmentX(0.5f);
		
		button_codestatistics = new JButton("行数统计");
		button_codestatistics.setAlignmentX(0.5f);
		
		button_toflutter = new JButton("转Flutter模型");
		
		button_tojava = new JButton("转java代码");
		
		
		 textField.setPreferredSize(new Dimension(640, 20));
		 textField.setMaximumSize(new Dimension(1920, 20));
		 box_v.add(textField);
		box_v.add(scrollPane);
		Box box_h= Box.createHorizontalBox();
		box_h.add(button);
//		box_h.add(Box.createRigidArea(new Dimension(10, 20)));
		box_h.add(button2);
//		box_h.add(Box.createRigidArea(new Dimension(10, 20)));
		box_h.add(button_format);
//		box_h.add(Box.createRigidArea(new Dimension(10,20)));
		box_h.add(button_xmlTojson);
		
		box_h.add(button_paramToJson);
		
		box_h.add(button_iToCode);
		box_h.add(button_mdToCode);
		box_h.add(button_codestatistics);
//		box_h.add(button_toflutter);
		box_h.add(button_tojava);
		
		box_h.setPreferredSize(new Dimension(640, 30));
//		JScrollPane scrollPane = new JScrollPane(box_h);
		

		box_v.add(box_h);
		mainJPanel.add(box_v);
		//设置最大宽高 用于适应布局
		//button.setPreferredSize(new Dimension(400, 60));
		button.setMaximumSize(new Dimension(screen_w,60));
		button2.setMaximumSize(new Dimension(screen_w, 60));
		button_format.setMaximumSize(new Dimension(screen_w, 60));
		button_xmlTojson.setMaximumSize(new Dimension(screen_w, 60));
		button_paramToJson.setMaximumSize(new Dimension(screen_w,60));
		button_iToCode.setMaximumSize(new Dimension(screen_w,60));
		button_mdToCode.setMaximumSize(new Dimension(screen_w,60));
		button_codestatistics.setMaximumSize(new Dimension(screen_w,60));
		button_tojava.setMaximumSize(new Dimension(screen_w,60));
		
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String textString=editArea.getText().toString();
				String jsonNameString= textField.getText();
				
				JsonToCode jsonToCode= new JsonToCode();
				if(jsonNameString.length()!=0){
					jsonToCode.setJsonObjectName(jsonNameString);
				}
				jsonToCode.setJson(textString);
				StringBuffer buffer = new StringBuffer();
				jsonToCode.getCode2(buffer,jsonNameString,new JSONObject(textString));
				textWindow.setText(buffer.toString());
				ClipBoard.setText(buffer.toString());
				textWindow.setVisible(true);
				textWindow.setState(JFrame.NORMAL);
				//editArea.setText(textString);
			}
		});
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String textString=editArea.getText().toString();
				String jsonNameString= textField.getText();
				
				textWindow.setText(ParameterToCode.toCode(jsonNameString, textString));
				ClipBoard.setText(ParameterToCode.toCode(jsonNameString, textString));
				textWindow.setState(JFrame.NORMAL);
				textWindow.setVisible(true);
			}
		});
		button_format.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String jsonFormatText= JsonFormat.formatJson(editArea.getText());
				editArea.setText(jsonFormatText);
				ClipBoard.setText(jsonFormatText);
			}
		});
		button_xmlTojson.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String text = editArea.getText();
				XmlToJson xmlToJson = new XmlToJson(text);
				ClipBoard.setText(xmlToJson.check("UTF-8")); 
			}
		});
		button_paramToJson.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String text = editArea.getText();
				
				editArea.setText(ParamTojson.toJson(text));
				
			}
		});
		button_iToCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = editArea.getText();
				String retext = ParamToCode.getCode(text);
				editArea.setText(retext);
			}
		});
		button_mdToCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = editArea.getText();
				String retext = MarkDownToCode.toCode(text);
				editArea.setText(retext);
			}
		});
		button_codestatistics.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = editArea.getText();
				int line = CodeUtil.getLines(text);
				editArea.setText("行数："+line);
				Toast.makeText(JSONToCodeWindow.this, "行数："+line).show();
			}
		});
		button_tojava.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String textString=editArea.getText().toString();
				String jsonNameString= textField.getText();
				
				JsonToCode jsonToCode= new JsonToCode();
				if(jsonNameString.length()!=0){
					jsonToCode.setJsonObjectName(jsonNameString);
				}
				jsonToCode.setJson(textString);
				
				String temp = jsonToCode. getModelJavaString("  ",jsonNameString,new JSONObject(textString));
				textWindow.setText(temp);
//				ClipBoard.setText(buffer.toString());
				textWindow.setVisible(true);
				textWindow.setState(JFrame.NORMAL);
				
			}
		});
		setSize(new Dimension(820, 480));
		setLocation((screen_w-640)/2, (screen_h-480)/2);
		textWindow.setLocation((screen_w-640)/2, (screen_h-480)/2);
		setTitle("json转代码v1.1 - 风的影子 - 2021.4.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("json.png")));
		//setVisible(true);
	}
}
