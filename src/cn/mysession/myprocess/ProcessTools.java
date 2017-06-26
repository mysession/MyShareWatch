package cn.mysession.myprocess;

import java.io.File;

public class ProcessTools {

	/**
	 * 判断这个文件是否可以进行操作
	 * @param file
	 * @return
	 */
	public static boolean isOK(File file) {
		if(file.renameTo(file)){  
//		  System.out.println("文件未被操作"); 
			return true;
		}else{  
//		  System.out.println("文件正在被操作");
			return false;
		}
	}
}
