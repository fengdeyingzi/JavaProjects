import java.awt.EventQueue;
import java.io.File;
 
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
 

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
 
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
 
public class PlayerMain {
	static MyWindow frame;
 
	public static void main(String[] args) {
 
		 NativeLibrary.addSearchPath(  
				    RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC\\sdk\\lib"); 
				  Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);  
				  
				  EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								frame = new MyWindow();
								frame.setTitle("VLC播放器");
								frame.setVisible(true);
								frame.getMediaPlayer().prepareMedia("file:///J:\\Videos\\浏览Github必备的5款神器级别的Chrome插件.flv");
								new SwingWorker<String , Integer>() {
 
									@Override
									protected String doInBackground() throws Exception {
										while(true){
										long total = frame.getMediaPlayer().getLength();
										long curr = frame.getMediaPlayer().getTime();
										float percent = (float)curr/total;
										publish((int)(percent*100));
										Thread.sleep(100);
										}
									}
									
									protected void process(java.util.List<Integer> chunks) {
										
										for (int v:chunks) {
											frame.getProgressBar().setValue(v);
										}
									};
 
								}.execute();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
	}
	//播放按钮
	public static void play(){
		frame.getMediaPlayer().play();
		System.out.println("当前时间:"+frame.getMediaPlayer().getTime());
	}
	//暂停按钮
	public  static void  pause() {
		frame.getMediaPlayer().pause();
	}
	//停止按钮
	public static void stop(){
		frame.getMediaPlayer().stop();
	}
	//跳转按钮
	public static void jumpTo(float to){
		frame.getMediaPlayer().setTime((long)(to*frame.getMediaPlayer().getLength()));
	}
	//音量按钮
	public static void setvol(int v){
		frame.getMediaPlayer().setVolume(v);
	}
	//打开文件
	public static void openVideo(){
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v== JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().playMedia(file.getAbsolutePath());
		}
	}
	//退出
	public static void exit(){
		frame.getMediaPlayer().release();
		System.exit(0);
	}
}