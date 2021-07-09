package com.xl.window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONObject;

import com.xl.util.FileUtils;

import mrpbuilder_java.MrpBuilder;
import mrpbuilder_java.MrpBuilder.Config;
import mrpbuilder_java.MrpBuilder.FileItem;
import mrpbuilder_java.MrpUnpack;

public class BuildWindow extends JFrame {

	JList list;
	JButton btn_add;
	JButton btn_delete;
	JButton btn_build;
	JButton btn_help;
	JTextField edit_filename, edit_displayname, edit_vender, edit_desc, edit_path, edit_appid, edit_version;
	JTextField edit_mrppath;
	JButton btn_unpack;
	ArrayList<String> list_file;
	private JFileChooser fileChooser = new JFileChooser();

	public BuildWindow() {
		setTitle("MrpBuilder_V1.0 - 风的影子制作 2021.7.9");
		setSize(640, 480);
		list_file = new ArrayList<String>();
		JPanel layout_main = new JPanel();
		setContentPane(layout_main);
		list = new JList<String>();
		btn_add = new JButton("添加");
		btn_delete = new JButton("删除");
		btn_build = new JButton("打包");
		btn_help = new JButton("打包说明");
		edit_filename = new JTextField();
		edit_filename.setToolTipText("文件名");
		edit_filename.setColumns(30);
		Box box_item1 = Box.createHorizontalBox();
		box_item1.add(new JLabel("文件名："));
		box_item1.add(edit_filename);
		box_item1.setMinimumSize(new Dimension(400, 32));
		
		edit_displayname = new JTextField();
		edit_displayname.setToolTipText("显示名");
		
		
		edit_vender = new JTextField();
		edit_vender.setToolTipText("供应商");
		
		edit_desc = new JTextField();
		edit_desc.setToolTipText("详情");
		
		edit_path = new JTextField();
		edit_path.setToolTipText("输出路径");
		edit_appid = new JTextField();
		edit_appid.setToolTipText("appid");
		
		edit_version = new JTextField();
		edit_version.setToolTipText("版本号");

		Box box_v = Box.createVerticalBox();
		box_v.setMaximumSize(new Dimension(1080, 320));
		box_v.add(box_item1);
		box_v.add(createHorBox("显示名", edit_displayname));
		box_v.add(createHorBox("供应商", edit_vender));
		box_v.add(createHorBox("详情", edit_desc));
		box_v.add(createHorBox("输出路径", edit_path));
		box_v.add(createHorBox("appid", edit_appid));
		box_v.add(createHorBox("版本号", edit_version));
		edit_appid.setText("30000");
		edit_version.setText("1000");
		Box box_h = Box.createHorizontalBox();
		box_h.setMaximumSize(new Dimension(1080, 64));
		box_h.add(btn_add);
		box_h.add(btn_delete);
		box_h.add(btn_build);
		box_h.add(btn_help);
		
		box_v.add(list);
		Box box_h2 = Box.createHorizontalBox();
		edit_mrppath = new JTextField();
		btn_unpack = new JButton("解包mrp");
		box_h2.add(edit_mrppath);
		box_h2.add(btn_unpack);
		
		JComponent component = new JComponent() {
		};
		component.setPreferredSize(new Dimension(30, 30));

		list.setMaximumSize(new Dimension(1080, 200));
		list.setListData(new String[] { });

		box_v.add(box_h);
		box_v.add(component);
		box_v.add(box_h2);
//		box_v.setMinimumSize(new Dimension(640, 320));
		layout_main.add(box_v);

		btn_add.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int i = fileChooser.showOpenDialog(getContentPane());// 显示文件选择对话框

				// 判断用户单击的是否为“打开”按钮
				if (i == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fileChooser.getSelectedFile();// 获得选中的文件对象
					String path = selectedFile.getPath();// 显示选中文件的名称
					list_file.add(path);
					updateData();
				}
			}
		});
		btn_delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				int pos = list.getSelectedIndex();
				if (pos >= 0 && pos <= list_file.size()) {
					list_file.remove(pos);
					updateData();
				}

			}
		});
		btn_build.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				build();

			}
		});
		btn_help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				TextWindow window = new TextWindow();
				String jsonText = 
						"{\n"
						+"    \"display\": \"CAPP\",\n"
						+"    \"filename\": \"demo.mrp\",\n"
						+"    \"appid\": 40001,\n"
						+"    \"path\":\"out.mrp\",\n"
						+"    \"version\": 100,\n"
						+"    \"vendor\": \"风的影子\",\n"
						+"    \"description\": \"desc\",\n"
						+"    \"files\": [\n"
						+"        \"/lib/start.mr\",\n"
						+"        \"/lib/cfunction.ext\"\n"
						+"    ]\n"
						+"}";
				window.setText("打包时，可以添加  pack.json 文件，将解析这个文件信息自动填入编辑框\n\npack.json文件内容示例：\n"+jsonText);
				window.setTitle("打包说明");
				window.setVisible(true);
			}
		});
		btn_unpack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				String path = edit_mrppath.getText();

				MrpUnpack unpack = new MrpUnpack(new File(path));
				unpack.unpack(new File(path).getParent());

			}
		});
		dragFile(edit_path, new OnDragFile() {

			@Override
			public void onDragFile(List<File> list_file) {
				edit_path.setText(list_file.get(0).getPath());

			}
		});
		dragFile(edit_mrppath, new OnDragFile() {

			@Override
			public void onDragFile(List<File> list_file) {
				edit_mrppath.setText(list_file.get(0).getPath());

			}
		});
		dragFile(btn_add, new OnDragFile(){

			@Override
			public void onDragFile(List<File> list) {
				for(File fileitem : list){
					list_file.add(fileitem.getAbsolutePath());
					updateData();
				}
				
			}
			
		});
	}
	
	public Box createHorBox(String name, Component component){
		Box box_item = Box.createHorizontalBox();
		box_item.add(new JLabel(name));
		box_item.add(component);
		return box_item;
	}

	public void updateData() {
		list.setListData(list_file.toArray());
		for(String item:list_file){
			if(FileUtils.getName(item).equals("pack.json")){
				parseJsonFile(item);
			}
		}
	}

	public void dragFile(Component c, final OnDragFile onDragFile) {
		new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				try {

					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						List<File> list = (List<File>) (dtde.getTransferable()
								.getTransferData(DataFlavor.javaFileListFlavor));
						if (onDragFile != null) {
							onDragFile.onDragFile(list);
						}
						// String temp="";
						// for(File file:list)
						// {
						// temp+=file.getAbsolutePath()+";\n";
						// JOptionPane.showMessageDialog(null, temp);
						// dtde.dropComplete(true);
						//
						// }

					}

					else {

						dtde.rejectDrop();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

	}

	public interface OnDragFile {
		public void onDragFile(List<File> list_file);
	}
	
	public static String readText(File file, String encoding) throws IOException {
		String content = "";
		// File file = new File(path);

		if (file.isFile()) {
			FileInputStream input = new FileInputStream(file);

			byte[] buf = new byte[input.available()];
			input.read(buf);
			content = new String(buf, encoding);
		}
		return content;
	}
	
	//解析json file
	public void parseJsonFile(String filepath){
		// 读取数据
				String jsonPath = "./pack.json";
				if(filepath!=null){
					jsonPath = filepath;
				}
				
				File f = new File(this.getClass().getResource("/").getPath());
				System.out.println(f);
				File file = new File(jsonPath);
				if (!file.exists()) {
					System.out.println("file not found");
					System.exit(0);
				}
				String jsonText = "";
				try {
					jsonText = readText(file, "UTF-8");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject jsonObject = new JSONObject(jsonText);
				Config config = new MrpBuilder().new Config();
				config.Appid = jsonObject.optInt("appid");
				config.DisplayName = jsonObject.optString("display");
				config.FileName = jsonObject.optString("filename");
				config.path = new File(f, jsonObject.optString("path")).getPath();
				if (config.path.length() == 0)
					config.path = config.FileName;
				config.Version = jsonObject.optInt("version");
				config.Vendor = jsonObject.optString("vendor");
				config.Desc = jsonObject.optString("description");
				config.AuthStr = jsonObject.optString("auth");
				config.Flag = jsonObject.optInt("flag");
				config.ScreenWidth = jsonObject.optInt("screen_width");
				config.ScreenHeight = jsonObject.optInt("screen_height");
				config.Plat = (byte) jsonObject.optInt("platform");
				System.out.println("Vendor " + config.Vendor);
				System.out.println("Desc " + config.Desc);
				edit_filename.setText(config.FileName);
				edit_displayname.setText(config.DisplayName);
				edit_vender.setText(config.Vendor);
				edit_desc.setText(config.Desc);
				edit_path.setText(config.path);
				edit_appid.setText(""+config.Appid);
				edit_version.setText(""+config.Version);
	}

	public void build() {
		ArrayList<File> list_file = new ArrayList<File>();
		for (String path : this.list_file) {
			list_file.add(new File(path));
		}
		MrpBuilder.Config config = new MrpBuilder().new Config();
		config.FileName = edit_filename.getText();
		config.DisplayName = edit_displayname.getText();
		config.Vendor = edit_vender.getText();
		config.Desc = edit_desc.getText();
		config.path = edit_path.getText();
		config.Appid = Integer.valueOf(edit_appid.getText());
		config.Version = Integer.valueOf(edit_version.getText());
		config.AuthStr = "";
		config.list_file = new ArrayList<MrpBuilder.FileItem>();
		MrpBuilder builder = new MrpBuilder();
		builder.pack(config, list_file);

	}

}
