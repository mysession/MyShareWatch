import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;

public class JnotifyDemo2 {

	
	public static void main(String[] args) throws JNotifyException {
		MyListener m = new MyListener();
		
		String path = "\\\\ASUS\\Users";
		String path2 = "\\\\ERROR\\Users";
		String path3 = "\\\\ERROR\\���ģʽDesignPattern";
//		int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
		int mask = JNotify.FILE_CREATED | JNotify.FILE_RENAMED;
		JNotify.addWatch(path, mask, true, m);
		JNotify.addWatch(path2, mask, true, m);
		JNotify.addWatch(path3, mask, true, m);
		
		System.out.println("�������");
		while (true) {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {}
		}
	}
}
class MyListener extends JNotifyAdapter {
	@Override
	public void fileCreated(int wd, String rootPath, String name) {
		System.out.println("����: " + rootPath + "\\" + name);
	}

	@Override
	public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
		System.out.println("������: " + rootPath + "\\" + newName);
	}

	@Override
	public void fileDeleted(int wd, String rootPath, String name) {
		System.out.println("ɾ��: " + rootPath + "\\" + name);
	}

	@Override
	public void fileModified(int wd, String rootPath, String name) {
		System.out.println("�޸�: " + rootPath + "\\" + name);
	}
	
	
}
