import java.util.ArrayList;
import java.util.Scanner;

import com.xl.util.PostorderTraversal;

public class FileTreeMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String root = "I:\\AndroidWorkSpace\\ProHBDetection\\Defalut\\2.0\\Android_Sys_HB_V2_Diagnosis_Code";
		System.out.println("请输入工程目录：");
		 root = sc.nextLine();
		PostorderTraversal traversal = new PostorderTraversal();
      int stop = 30;
      String[] list_ignores = new String[]{".git",".cxx", ".deploy_git",".gradle",".idea","build", "node_modules", ".DS_Store"};
      ArrayList<String> ignores = new ArrayList<>();
      for(String item:list_ignores){
    	  ignores.add(item);
      }
System.out.println(root);
      traversal. printTree(root, stop, ignores);
	}
}
