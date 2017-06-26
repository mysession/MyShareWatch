import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.mysession.core.Process;
import cn.mysession.core.ShareDirectory;
import cn.mysession.core.Show;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		System.out.println("�����ʵ�������繲�������----��ʼ����");
		System.out.println("������������Ŀ¼,��ȴ�...;");

		ShareDirectory shareDirectory = context.getBean("shareDirectory", ShareDirectory.class);
		Show show = context.getBean("show", Show.class);
		
		File getFileDir = context.getBean("copyFilePath", File.class);
		if (!getFileDir.exists())
			getFileDir.mkdir();
		
		Process process = context.getBean("process", Process.class);
		
		shareDirectory.addProcess(process);

		new AddDirThread(shareDirectory).start();

		shareDirectory.start();
	}
}

