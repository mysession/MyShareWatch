package cn.mysession.myprocess;

import java.io.File;

public class Test {

	@org.junit.Test
	public void fun() {
		File f = new File("F:\\�½� Microsoft Word �ĵ�.docx");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(f.delete());
	}
}
