package com.xl.window;

public class FundItem {
	String name;
	String id;
	double dwjz;  //当前净值
	double gsz; //估算净值
	double gzf; //估算涨幅  %
	
	public FundItem(String name,String id){
		this.name = name;
		this.id = id;
	}

}
