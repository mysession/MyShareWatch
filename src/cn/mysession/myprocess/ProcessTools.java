package cn.mysession.myprocess;

import java.io.File;

public class ProcessTools {

	/**
	 * �ж�����ļ��Ƿ���Խ��в���
	 * @param file
	 * @return
	 */
	public static boolean isOK(File file) {
		if(file.renameTo(file)){  
//		  System.out.println("�ļ�δ������"); 
			return true;
		}else{  
//		  System.out.println("�ļ����ڱ�����");
			return false;
		}
	}
}
