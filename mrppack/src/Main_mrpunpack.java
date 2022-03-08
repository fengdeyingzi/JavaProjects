import java.io.File;


import mrpbuilder_java.MrpUnpack;



public class Main_mrpunpack {

	public static void main(String[] args) {
		String path = "J:\\mythroad\\app240320\\1000_240320_gld.mrp";
		
		MrpUnpack unpack = new MrpUnpack(new File(path));
		unpack.unpack("D:\\Go\\");
	}
}
