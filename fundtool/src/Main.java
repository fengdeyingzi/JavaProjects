//import com.xl.swing.util.UIUtil;

import com.xl.util.FundUtil;
import com.xl.window.FundWindow;

import android.os.Looper;

public class Main {

	public static void main(String[] args) {
		// 启动looper	
        Looper.prepare(true);
//        UIUtil.setWindowsStyle();
        //
        FundWindow window = new FundWindow();
        window.setVisible(true);
        
        
        Looper.loop();
        
        
	}
}
