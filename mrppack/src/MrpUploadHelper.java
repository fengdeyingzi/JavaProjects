import java.io.File;
import java.util.Collection;

import com.xl.util.FileUtils;

import mrpbuilder_java.MrpUnpack;

/*
 * mrp 简单上传工具
 * insert into `mrp_list` (`appid`,`version`,`name`,`label`,`info`,`vendor`,`isShow`,`size`,`md5`,`addTime`) values ('{$appid}','{$version}','{$name}','{$label}','{$info}','{$vendor}','{$isshow}','{$size}','{$mmd5}','{$addtime}');
 */
public class MrpUploadHelper {
	

	public static void main(String[] args) {
		Collection<File> list_mrp = FileUtils.listFiles(new File("J:\\mythroad\\app_other\\"), new String[]{".mrp",".MRP"}, true);
		StringBuffer buffer = new StringBuffer();
		for(File item_mrp:list_mrp){
			MrpUnpack unpack = new MrpUnpack(item_mrp);
			MrpUnpack.Config config = unpack.getConfig();
			String md5 = FileUtils.getMD5(item_mrp.getAbsolutePath());
			System.out.println(String.format(
					"insert into `mrp_list` "
					+ "(`appid`,`version`,`name`,`label`,`info`,`vendor`,`isShow`,`size`,`md5`,`addTime`) values "
					+ "('%d','%d','%s','%s','%s','%s','1','%d','%s','%d');", 
					config.Appid, config.Version, config.FileName, config.DisplayName, config.Desc, config.Vendor, item_mrp.length()/1024, md5, (int)(System.currentTimeMillis()/1000)));
			buffer.append(String.format(
					"insert into `mrp_list` "
					+ "(`appid`,`version`,`name`,`label`,`info`,`vendor`,`isShow`,`size`,`md5`,`addTime`) values "
					+ "('%d','%d','%s','%s','%s','%s','1','%d','%s','%d');", 
					config.Appid, config.Version, config.FileName, config.DisplayName, config.Desc, config.Vendor, item_mrp.length()/1024, md5, (int)(System.currentTimeMillis()/1000)));
			buffer.append("\r\n");
		    FileUtils.copyFile(item_mrp, "J:\\out\\"+md5+".mrp");
		}
		System.out.println();
		System.out.println(buffer.toString());
		FileUtils.writeText("J:\\mrp.sql", buffer.toString(), "UTF-8");
	}
}
