package android.content;

import java.io.File;

import javax.swing.JFrame;

import android.content.res.Resources;

public interface Context {
	  public abstract File getFilesDir();
	  public abstract File getCacheDir();
	  public abstract File getExternalCacheDir();
	  public abstract File[] getExternalMediaDirs();
	  public abstract String[] fileList();
	  public abstract File getDir(String name, int mode);
	  public abstract Resources getResources();
	  public abstract JFrame getJFrame();
//	  public abstract void startActivity( Intent intent);
}
