package android.app;
import java.io.File;

import javax.swing.JFrame;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;


public class Activity implements Context{
	private JFrame frame;
	
	public Activity(JFrame jframe){
		this.frame = jframe;
	}
	
	public JFrame getJFrame(){
		return this.frame;
	}

	public File getFilesDir() {
		File file = new File("files");
		if(!file.exists()){
			file.mkdir();
		}
		return file;
	}

	public File getCacheDir() {
		File file = new File("cache");
		if(!file.exists())file.mkdir();
		return file;
	}

	public File getExternalCacheDir() {
		File file = new File("cache");
		if(!file.exists())file.mkdir();
		return file;
	}

	public File[] getExternalMediaDirs() {
		File file = new File("media");
		if(!file.exists())file.mkdir();
		File[] file1 = new File[1];
		file1[0] = file;
		return file1;
	}

	public String[] fileList() {
		File file = new File("");
		return file.list();
	}

	public File getDir(String name, int mode) {
		File file = new File(name);
		if(!file.exists())file.mkdir();
		return file;
	}

	public Resources getResources() {
		return new Resources();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		
	}
	
	public void setContentView(View view){
		this.getJFrame().add(view);
	}
	
	protected void onStart(){
		
	}
	
	protected void onResume() {
		
	}
	
	protected void onPause() {
		
	}
	
	protected void onStop() {
		
	}
	


	protected void onDestroy() {
		
	}

}
