package com.zz.check.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.zz.check.CheckConfig;
import com.zz.check.JarChecker;
import com.zz.check.meta.JarInfo;
import com.zz.check.util.JarConflictDetectUtil;

/**
 * 软件界面
 * 
 * @author zys
 *
 */
public class JarDetectAPP {

	private JFrame frmJar;
	private JTextField jarDir;
	private JTextArea resultArea;
	private JPanel panel = new JPanel();
	private JCheckBox checkAll = new JCheckBox("全部");
	private JCheckBox dependChk = new JCheckBox("依赖问题");
	private JCheckBox conflictChk = new JCheckBox("冲突问题");
	private JCheckBox missChk = new JCheckBox("缺失问题");
	private JCheckBox compileChk = new JCheckBox("编译问题");
	@SuppressWarnings("rawtypes")
	private JComboBox jdkcomboBox = new JComboBox();
	private JProgressBar bar = new JProgressBar();// 检测进度条
	private JButton detectBtn = new JButton("执行检测");
	private CheckConfig checkConfig = new CheckConfig();
	
	private final String inputTip = "请输入或选择jar包所在目录,多目录以  ; 隔开";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JarDetectAPP window = new JarDetectAPP();
					window.frmJar.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JarDetectAPP() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJar = new JFrame();
		frmJar.setTitle("jar包检测");
		frmJar.setResizable(false);
		frmJar.setSize(643, 500);
		frmJar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJar.getContentPane().setLayout(null);
		frmJar.setLocationRelativeTo(null);// 居中显示

		panel.setLayout(null);
		panel.setBounds(5, 5, 629, 93);
		frmJar.getContentPane().add(panel);

		// 检测目录
		setJardirInput();

		// 结果显示区域
		setResultArea();

		// 初始化checkbox
		initCheckBox();

		// 选择按钮设置
		setFileSelBtn();

		// 检测按钮设置
		setDetectBtn();

		// 清空按钮设置
		setClearBtn();

		// 导出按钮设置
		setExportBtn();

		// 进度条
		bar.setBounds(20, 29, 428, 20);
		bar.setBackground(UIManager.getColor("CheckBox.highlight"));
		bar.setVisible(false);
		panel.add(bar);

	}

	/**
	 * 检测目录
	 */
	private void setJardirInput() {
		jarDir = new JTextField(inputTip);
		jarDir.setBounds(20, 29, 428, 21);
		panel.add(jarDir);
		jarDir.setColumns(10);
		jarDir.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				jarDir.selectAll();
				bar.setVisible(false);
			}

			public void focusLost(FocusEvent e) {
			}
		});
	}

	/**
	 * 结果显示区域
	 */
	private void setResultArea() {

		JLabel label = new JLabel("检测结果");
		label.setBounds(3, 76, 60, 15);
		panel.add(label);

		resultArea = new JTextArea();
		resultArea.setRows(18);
		resultArea.setBounds(5, 10, 200, 374);
		resultArea.setColumns(70);
		resultArea.setTabSize(80);
		resultArea.setWrapStyleWord(true);

		JScrollPane scr = new JScrollPane(resultArea);
		scr.setLocation(5, 103);
		scr.setSize(629, 359);
		scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmJar.getContentPane().add(scr);
	}

	/**
	 * 清空按钮设置
	 */
	private void setClearBtn() {
		JButton clearBtn = new JButton("清空");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultArea.setText("");
			}
		});
		clearBtn.setBounds(488, 70, 60, 23);
		panel.add(clearBtn);
	}

	/**
	 * 检测按钮设置
	 */
	private void setDetectBtn() {
		detectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doJarDetect();
			}
		});
		detectBtn.setBounds(526, 28, 93, 23);
		panel.add(detectBtn);
	}

	/**
	 * 设置文件选择按钮
	 */
	private void setFileSelBtn() {
		JButton fileSelBtn = new JButton("选择");
		fileSelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("请选择jar包目录");
				int result = chooser.showOpenDialog(frmJar);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					jarDir.setText(file.toString());
					doJarDetect();
				}
			}
		});
		fileSelBtn.setBounds(458, 28, 63, 23);
		panel.add(fileSelBtn);
	}

	/**
	 * 导出按钮
	 */
	private void setExportBtn() {
		JButton exportBtn = new JButton("导出");
		exportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte[] bytes = resultArea.getText().getBytes();
				if (bytes.length == 0) {
					return;
				}

				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				chooser.setMultiSelectionEnabled(false);
				chooser.setAcceptAllFileFilterUsed(false);
				File file = new File("result.txt");
				chooser.setSelectedFile(file);
				int result = chooser.showSaveDialog(frmJar);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					try {

						FileOutputStream out = new FileOutputStream(file);
						out.write(bytes, 0, bytes.length);
						out.flush();
						out.close();
						JOptionPane.showMessageDialog(frmJar, "结果导出成功！");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		exportBtn.setBounds(556, 70, 60, 23);
		panel.add(exportBtn);
	}

	/**
	 * 初始化checkbox
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initCheckBox() {

		final JLabel lb1 = new JLabel("【高于");
		lb1.setVisible(false);
		lb1.setBounds(405, 8, 40, 15);
		panel.add(lb1);
		jdkcomboBox.setModel(new DefaultComboBoxModel(new String[] { "1.7", "1.8", "1.6", "1.5", "1.4" }));
		jdkcomboBox.setBounds(444, 7, 44, 18);
		jdkcomboBox.setVisible(false);
		panel.add(jdkcomboBox);
		final JLabel lb2 = new JLabel("】");
		lb2.setBounds(490, 8, 14, 15);
		lb2.setVisible(false);
		panel.add(lb2);

		dependChk.setToolTipText("检测jar包间是否存在循环依赖问题");
		dependChk.setBounds(95, 4, 78, 23);
		panel.add(dependChk);

		conflictChk.setToolTipText("检测jar包间是否有冲突(含有重复类)");
		conflictChk.setBounds(173, 4, 78, 23);
		panel.add(conflictChk);

		missChk.setToolTipText("检测是各jar包否存在引用类文件缺失问题");
		missChk.setBounds(252, 4, 78, 23);
		panel.add(missChk);

		compileChk.setToolTipText("检测是否存在编译时jdk版本高于运行时jdk版本的jar包");
		compileChk.setBounds(331, 4, 77, 23);
		compileChk.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean state = compileChk.isSelected();
				lb1.setVisible(state);
				lb2.setVisible(state);
				jdkcomboBox.setVisible(state);

			}
		});
		panel.add(compileChk);

		checkAll.setBounds(20, 4, 60, 23);
		panel.add(checkAll);
		checkAll.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean allchkState = checkAll.isSelected();
				dependChk.setSelected(allchkState);
				conflictChk.setSelected(allchkState);
				missChk.setSelected(allchkState);
				compileChk.setSelected(allchkState);
			}
		});
	}

	/**
	 * 执行检测
	 */
	private void doJarDetect() {
		resultArea.setText("");
	    
		final JarChecker checker = new JarChecker(checkConfig);

		final String filePath = jarDir.getText();// 检测的目录
		if (inputTip.equals(filePath)) {
			JOptionPane.showMessageDialog(frmJar, "请输入或选择要检测的目录！");
			return;
		}
		// 如果是多个目录
		if(filePath.indexOf(";")>-1){
			String[] dirs = filePath.split(";");
			File file;
			for(String dir : dirs){
				file = new File(dir);
				if(!file.isDirectory()){
					JOptionPane.showMessageDialog(frmJar, dir+" 不是正确的目录");
					return;
				}
			}
		}else{
			File file = new File(filePath);
			if (!file.isDirectory()) {
				JOptionPane.showMessageDialog(frmJar, "请输入或选择正确的jar包目录！");
				return;
			}
		}
		
		
		int jarFileCount = checker.getJarFileCount(filePath);
		if (jarFileCount == 0) {
			JOptionPane.showMessageDialog(frmJar, "您输入的目录下没有jar文件！");
			return;
		}
		if (!dependChk.isSelected() && !missChk.isSelected() && !conflictChk.isSelected() && !compileChk.isSelected()) {
			JOptionPane.showMessageDialog(frmJar, "请选择要检查的问题！");
			return;
		}

		bar.setVisible(true);
		bar.setStringPainted(true);
		bar.setIndeterminate(true);
		bar.setMaximum(jarFileCount);
		
		detectBtn.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				checker.check(filePath);
			}
		}).start();
		new Thread(new ProgressBarThread(bar, checker, this)).start();

	}

	/**
	 * 设置结果信息
	 * 
	 * @param checker
	 */
	public void setResult(JarChecker checker) {

		List<JarInfo> list = checker.getCheckResult();
		String conflictStr = "";// 冲突检测结果
		String compileStr = "";// 编译问题
		String missStr = "";// 缺失问题
		String cycleStr = "";// 循环依赖问题
		String result = "";
		Map<String, Set<String>> confictMap = new HashMap<String, Set<String>>();
		String filePath = jarDir.getText();

		// 循环依赖问题
		cycleStr = "================================依赖问题==================================\n";
		if (list.get(0).getCycleJars().isEmpty()) {
			cycleStr += "没有发现循环依赖";
		} else {
			Set<String> cycles = list.get(0).getCycleJars();
			for (String oneCycle : cycles) {
				cycleStr += oneCycle + "\n";
			}
		}

		// 冲突问题
		conflictStr = "================================冲突问题==================================\n";
		if (conflictChk.isSelected()) {
			String ret = "";
			confictMap = new JarConflictDetectUtil(checkConfig).getConflict(filePath);
			if (confictMap.isEmpty()) {
				ret = "该目录下无jar包冲突";
			} else {
				for (String jarname : confictMap.keySet()) {
					ret += jarname + "\n" + confictMap.get(jarname).toString() + "\n";
				}
			}
			conflictStr += ret;
		}

		// 缺失问题
		missStr = "================================缺失问题==================================\n";
		Set<String> trouble;
		Set<String> alltrouble = new HashSet<String>();
		String missJars = "";
		for (JarInfo jarInfo : list) {
			trouble = jarInfo.getTroubleClasses();
			if (!trouble.isEmpty()) {
				alltrouble.addAll(trouble);
				missJars += jarInfo.getName() + "\n";
			}
		}
		if (!alltrouble.isEmpty()) {
			missStr += "以下jar包存在缺失问题" + "\n" + missJars + "\n所有的缺失类为：\n";
			for (String str : alltrouble) {
				missStr += str + "\n";
			}
		} else {
			missStr += "没有发现缺失问题";
		}

		// 编译问题
		compileStr = "================================编译问题==================================\n";
		List<JarInfo> badVerlist = new ArrayList<JarInfo>();
		int compareVer = getCompareVersion(jdkcomboBox.getSelectedItem().toString());
		for (JarInfo jarInfo : list) {
			if (jarInfo.getBuildVersion() > compareVer) {
				badVerlist.add(jarInfo);
			}
		}
		if (badVerlist.size() > 0) {
			for (JarInfo in : badVerlist) {
				compileStr += in.getName() + " , buildVersion=" + in.getBuildVersion() + "\n";
			}
		} else {
			compileStr += "没有发现编译问题";
		}

		if (dependChk.isSelected()) {
			result += cycleStr + "\n";
		}
		if (conflictChk.isSelected()) {
			result += conflictStr + "\n";
		}
		if (missChk.isSelected()) {
			result += missStr + "\n";
		}
		if (compileChk.isSelected()) {
			result += compileStr + "\n";
		}
		resultArea.setText(result);
		detectBtn.setEnabled(true);
	}

	/**
	 * 取得版本号，只取major
	 * 
	 * @param jdkVersion
	 * @return
	 */
	private int getCompareVersion(String jdkVersion) {
		// 48 1.4
		// 49 1.5
		// 50 1.6
		// 51 1.7
		// 52 1.8
		int ver = 0;
		if("1.4".equals(jdkVersion))ver = 48;
		if("1.5".equals(jdkVersion))ver = 49;
		if("1.6".equals(jdkVersion))ver = 50;
		if("1.7".equals(jdkVersion))ver = 51;
		if("1.8".equals(jdkVersion))ver = 52;
		
		return ver;
	}

	/**
	 * 进度条显示
	 * 
	 * @author zys
	 *
	 */
	class ProgressBarThread implements Runnable {
		JProgressBar bar;
		JarChecker checker;
		JarDetectAPP detect;
		boolean isOver = false;
		Runnable run = null;

		public ProgressBarThread(final JProgressBar bar, final JarChecker checker, final JarDetectAPP detect) {
			this.bar = bar;
			this.checker = checker;
			this.detect = detect;
			run = new Runnable() {
				@Override
				public void run() {
					int completCnt = 0;
					int max = bar.getMaximum();
					completCnt = checker.getCompleteCount();
					bar.setString("检测中... " + completCnt + " / " + max);
					if (completCnt == max + 1) {
						bar.setString("全部检测完成 ,共计" + max + "个jar");
						isOver = true;
					}
				}
			};
		}

		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(150);
					if (!isOver) {
						SwingUtilities.invokeLater(run);
					} else {
						// 全部检测完成后设置结果显示信息
						detect.setResult(checker);
						bar.setIndeterminate(false);
						break;
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
