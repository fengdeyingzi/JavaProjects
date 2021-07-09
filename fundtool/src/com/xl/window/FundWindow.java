package com.xl.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xl.tool.OConnect;
import com.xl.util.FileUtils;
import com.xl.util.FundUtil;

import javax.swing.JList;
import javax.swing.Action;
import javax.swing.JButton;

public class FundWindow extends JFrame {

	private JPanel contentPane;

	
	List<FundItem> list_fund;
	JList list;
	int i=0;
	
	void getList(final List<FundItem> list_fund,final ArrayList<String> list_text){
		if(list_fund.size()>0){
			final FundItem item = list_fund.get(0);
			list_fund.remove(0);
		FundUtil.getFundNameByCode(item.id, new OConnect.PostGetInfoListener() {
			
			@Override
			public void onPostGetText(String text) {
				String resultStr = text;
				if (resultStr.length()>0 && resultStr.indexOf("(")>=0 && resultStr.lastIndexOf(")")>=0) {
					resultStr = resultStr.substring(resultStr.indexOf("(") + 1, resultStr.lastIndexOf(")"));
					System.out.println(resultStr);
					try{
						JSONObject obj_code = new JSONObject(resultStr);
					String dwjz = obj_code.getString("dwjz");
					String gsz = obj_code.getString("gsz");
					item.dwjz = Double.parseDouble(dwjz);
					item.gsz = Double.parseDouble(gsz);
					item.gzf = ((item.gsz/item.dwjz)-1)*100;
					
					list_text.add(String.format("%s %s    估算涨幅：%.2f%%",i, item.name, item.gzf));
					
					list.setListData(list_text.toArray());
					getList(list_fund, list_text);
//					SwingUtilities.updateComponentTreeUI(FundWindow.this);
					}catch(JSONException e){
						list_text.add(String.format("%s %s    error",i, item.name));
						
						list.setListData(list_text.toArray());
						getList(list_fund, list_text);
					}
					
		        }
				else{
					list_text.add(String.format("%s %s    error2",i, item.name));
					
					list.setListData(list_text.toArray());
					getList(list_fund, list_text);
				}
			}
		});
		}
		else{
			
		}
		
	}
	
	void refFund(){
		i++;
		final ArrayList<String> list_text = new ArrayList<String>();
//		ListModel model = list.getModel();
		
		list_fund.clear();
		
		
		list.removeAll();
		SwingUtilities.updateComponentTreeUI(FundWindow.this);
		File path = new File(this.getClass().getResource("/").getPath());
		File jsonFile = new File(path,"fund.json");
		if(jsonFile.isFile()){
			try {
			JSONObject jsonObject = new JSONObject(FileUtils.readText(jsonFile, "UTF-8"));
			JSONArray array_fund = jsonObject.getJSONArray("list");
			for(int i=0;i<array_fund.length();i++){
				JSONObject item = array_fund.getJSONObject(i);
				String name = item.getString("name");
				String id = item.getString("id");
				list_fund.add(new FundItem(name,id));
				
			}
			getList(list_fund,list_text);
			/*
			for(final FundItem item:list_fund){
				FundUtil.getFundNameByCode(item.id, new OConnect.PostGetInfoListener() {
					
					@Override
					public void onPostGetText(String text) {
						String resultStr = text;
						if (resultStr.length()>0 && resultStr.indexOf("(")>=0 && resultStr.lastIndexOf(")")>=0) {
							resultStr = resultStr.substring(resultStr.indexOf("(") + 1, resultStr.lastIndexOf(")"));
							System.out.println(resultStr);
							try{
								JSONObject obj_code = new JSONObject(resultStr);
							String dwjz = obj_code.getString("dwjz");
							String gsz = obj_code.getString("gsz");
							item.dwjz = Double.parseDouble(dwjz);
							item.gsz = Double.parseDouble(gsz);
							item.gzf = ((item.gsz/item.dwjz)-1)*100;
							
							list_text.add(String.format("%s %s    估算涨幅：%.2f%%",i, item.name, item.gzf));
							
							list.setListData(list_text.toArray());
//							SwingUtilities.updateComponentTreeUI(FundWindow.this);
							}catch(JSONException e){
								list_text.add(String.format("%s %s    error",i, item.name));
								
								list.setListData(list_text.toArray());
							}
							
				        }
						else{
							list_text.add(String.format("%s %s    error2",i, item.name));
							
							list.setListData(list_text.toArray());
						}
					}
				});
			}*/
			 // 设置选项数据（内部将自动封装成 ListModel ）
		    
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			System.out.println(jsonFile.getPath()+"文件未找到");
		}
	}

	/**
	 * Create the frame.
	 */
	public FundWindow() {
		setTitle("基金估值 - 风的影子 - 2020.11.24");
		list_fund = new ArrayList<FundItem>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		 list = new JList();
		contentPane.add(list, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("刷新");
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
		
		
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refFund();
				
			}
		});
		refFund();
		
		
		
	}
	
	

}
