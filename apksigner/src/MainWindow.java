
import java.util.*;
import java.util.List;

import com.android.apksigner.ApkSignerTool;
import com.android.apksigner.ApkSignerTool2;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.DimensionUIResource;

import org.json.JSONException;
import org.json.JSONObject;

import com.xl.util.UIUtil;

public class MainWindow extends JFrame {

	private final JTextField textField_file;
	private final JTextField textfield_keypathField;
	private final JTextField textfield_passwordField;
	private final JTextField textfield_keynameField;
	private final JTextArea label_info;
	private JPanel panel = new JPanel();
	private JFileChooser fileChooser = new JFileChooser();

	public MainWindow() {
		setTitle("apk签名 - 风的影子 制作  - https://github.com/fengdeyingzi/apksigner");
		setBounds(400, 400, 450, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		Box box_v = Box.createVerticalBox();
//		panel.add(box_v);

		Box box_file = Box.createHorizontalBox();
		final JLabel label = new JLabel();
		label.setText("文件：");
		box_file.add(label);

		textField_file = new JTextField();
		textField_file.setColumns(20);
		textField_file.setMaximumSize(new Dimension(1090, 28));
		box_file.add(textField_file);

		final JButton button = new JButton("上传");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int i = fileChooser.showOpenDialog(getContentPane());// 显示文件选择对话框

				// 判断用户单击的是否为“打开”按钮
				if (i == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fileChooser.getSelectedFile();// 获得选中的文件对象
					textField_file.setText(selectedFile.getPath());// 显示选中文件的名称
				}
			}
		});
		box_file.add(button);
		box_v.add(box_file);
		JLabel label_keypathJLabel = new JLabel("key路径");
		textfield_keypathField = new JTextField();
		textfield_keypathField.setColumns(20);
		textfield_keypathField.setMaximumSize(new Dimension(1090, 28));
		final JButton button_setkey = new JButton("设置");
		button_setkey.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int i = fileChooser.showOpenDialog(getContentPane());// 显示文件选择对话框

				// 判断用户单击的是否为“打开”按钮
				if (i == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fileChooser.getSelectedFile();// 获得选中的文件对象
					textfield_keypathField.setText(selectedFile.getPath());// 显示选中文件的名称
				}
			}
		});

		JLabel lable_keynameJLabel = new JLabel("key名字");
		textfield_keynameField = new JTextField();
		textfield_keynameField.setMaximumSize(new Dimension(1090, 28));
		textfield_keynameField.setColumns(20);

		JLabel lable_passwordJLabel = new JLabel();
		lable_passwordJLabel.setText("key密码");
		textfield_passwordField = new JTextField();
		textfield_passwordField.setMaximumSize(new Dimension(1090, 28));
		textfield_passwordField.setColumns(20);

		Box panel_0 =  Box.createHorizontalBox();
		panel_0.setPreferredSize(new Dimension(640, 30));
		panel_0.setMaximumSize(new Dimension(1090, 30));
		panel_0.add(label_keypathJLabel);
		panel_0.add(textfield_keypathField);
		panel_0.add(button_setkey);

		Box panel_1 = Box.createHorizontalBox();
		panel_1.setPreferredSize(new Dimension(640, 30));
		panel_1.add(lable_keynameJLabel);
		panel_1.add(textfield_keynameField);

		Box panel_2 = Box.createHorizontalBox();
		panel_2.setPreferredSize(new DimensionUIResource(640, 30));
		panel_2.add(lable_passwordJLabel);
		panel_2.add(textfield_passwordField);

		Box panel_sign = Box.createHorizontalBox();
		JButton btn_sign = new JButton();
		btn_sign.setText("签名");
		btn_sign.setMaximumSize(new Dimension(1090, 30));
		panel_sign.add(btn_sign);

		JPanel panel_info = new JPanel();
		
		label_info = new JTextArea();
		Box box_info = Box.createHorizontalBox();
		JScrollPane jScrollPane = new JScrollPane(label_info);
//		label_info.setEditable(false);
		label_info.setText("");
		jScrollPane.setMaximumSize(new Dimension(1090, 480));
		jScrollPane.setPreferredSize(new Dimension(1090, 60));

		

		box_info.add(jScrollPane);
		

		box_v.add(panel_0);
		box_v.add(panel_1);
		box_v.add(panel_2);
//		box_v.add(panel_info);
		box_v.add(box_info);
		box_v.add(panel_sign);
		

		btn_sign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveConfig();
				// TODO Auto-generated method stub
				try {
					MainWindow.this.signApk(textField_file.getText(), textfield_keypathField.getText(),
							textfield_keynameField.getText(), textfield_passwordField.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

//		add(panel, BorderLayout.NORTH);
		setContentPane(box_v);

		setVisible(true);
		readConfig();
		dragFile(textField_file, new OnDragFile() {
			
			@Override
			public void onDragFile(List<File> list_file) {
				textField_file.setText(list_file.get(0).getPath());
			}
		});
		dragFile(textfield_keypathField, new OnDragFile() {
			
			@Override
			public void onDragFile(List<File> list_file) {
				textfield_keypathField.setText(list_file.get(0).getPath());
			}
		});
	}
	
    public void dragFile(Component c, final OnDragFile onDragFile)
    {
       new DropTarget(c,DnDConstants.ACTION_COPY_OR_MOVE,new DropTargetAdapter()
        {
           @Override
           public void drop(DropTargetDropEvent dtde)
           {
               try{

                   if(dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    {
                       dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                       List<File>list=(List<File>)(dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                       if(onDragFile!=null){
                           onDragFile.onDragFile(list);
                       }
//                     String temp="";
//                     for(File file:list)
//                      {
//                         temp+=file.getAbsolutePath()+";\n";
//                         JOptionPane.showMessageDialog(null, temp);
//                         dtde.dropComplete(true);
//                         
//                      }

                    }

                   else
                    {

                       dtde.rejectDrop();
                    }

               }catch(Exception e){e.printStackTrace();}

           }

        });

    }

 public interface OnDragFile{
     public void onDragFile(List<File> list_file);
 }

	public void signApk(String path, String key, String appkey, String password)
			throws IOException, InterruptedException {
		// 4----签名 （文件名称中不能包含空格）
		String jdkBinPath = "C:\\Program Files\\Java\\jdk1.6.0_26\\bin";
		String path_new = path.substring(0, path.length() - 4) + "_sign.apk";
		File bin = new File(jdkBinPath);
		File file_out = new File(path_new);
//		   file_out.createNewFile();
		String cmd = "jarsigner -keystore " + key + " -storepass " + password + " -keypass " + password + " -signedjar "
				+ path_new + " " + path + "" + " " + appkey+"\n";
		//// apksigner sign --ks (签名地址) --ks-key-alias (别名) --out (签名后的apk地址) (待签名apk地址)
		cmd = "--ks "+key+" --ks-pass pass:"+password+ " --ks-key-alias "+appkey+" --out "+path_new+" "+path+" "+"";
		System.out.println("" + cmd);
		label_info.append(cmd + "\r\n");
		try {
			String text = ApkSignerTool2.sign(cmd.split(" "));
			label_info.append(text+"\r\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		Process process = Runtime.getRuntime().exec(cmd, null);
		if (process.waitFor() != 0)
			System.out.println("签名失败。。。");
		else {
			System.out.println("签名成功");
			label_info.append("签名成功\r\n");
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = input.readLine()) != null) {
			System.out.println(line);
			label_info.append(line + "\r\n");
		}
		input.close();
		*/
		// jarsigner -verbose -keystore /Users/mac/Downloads/xl.keystore -signedjar
		// "/Users/mac/Downloads/app-release_legu.apk"
		// "/Users/mac/Downloads/app-release_legu_sign.apk" appkey

	}
	
	//读取配置文件
	void readConfig(){
		File file = new File("apkaigner_config.json");
		String text=null;
		try {
			text = FileUtils.read(file, "UTF-8");
			try{
			JSONObject jsonObject = new JSONObject(text);
			String apkfile = jsonObject.getString("apkfile");
			String keyfile = jsonObject.getString("keyfile");
			String keyname = jsonObject.getString("keyname");
			textField_file.setText(apkfile);
			textfield_keypathField.setText(keyfile);
			textfield_keynameField.setText(keyname);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	//保存配置文件
	void saveConfig(){
		File file = new File("apkaigner_config.json");
		String apkfile = textField_file.getText();
		String keyfile = textfield_keypathField.getText();
		String keyname = textfield_keynameField.getText();
		try{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("apkfile", apkfile);
		jsonObject.put("keyfile", keyfile);
		jsonObject.put("keyname", keyname);
		FileUtils.writeText("apkaigner_config.json", jsonObject.toString(), "UTF-8");
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UIUtil.setWindowsStyle();
		MainWindow test = new MainWindow();

	}

}