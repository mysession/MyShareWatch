
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;

/**
 * 
 * @author zxn 2014-08-10
 */
public class JnotifyDemo extends JNotifyAdapter {
	private static final String REQUEST_BASE_PATH = "\\\\ERROR\\Users";
	/** �����ӵ�Ŀ¼ */
	String path = REQUEST_BASE_PATH;
	/** ��עĿ¼���¼� */
//	int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
	int mask = JNotify.FILE_CREATED | JNotify.FILE_RENAMED;
	/** �Ƿ������Ŀ¼������������ */
	boolean watchSubtree = true;
	/** ��������Id */
	public int watchID;

	public static void main(String[] args) {
		new JnotifyDemo().beginWatch();
	}

	/**
	 * ��������ʱ�������ӳ���
	 * 
	 * @return
	 */
	public void beginWatch() {
		/** ��ӵ����Ӷ����� */
		try {
			this.watchID = JNotify.addWatch(path, mask, watchSubtree, this);
			System.err.println("jnotify -----------�����ɹ�-----------");
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
		// ��ѭ�����߳�һֱִ�У�����һ���Ӻ����ִ�У���Ҫ��Ϊ�������߳�һֱִ��
		// ����ʱ��ͼ���ļ�������Ч���޹أ�����˵���Ǽ���Ŀ¼�ļ��ı�һ���Ӻ�ż�⵽����⼸����ʵʱ�ģ����ñ���ϵͳ�⣩
		while (true) {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {// ignore it
			}
		}
	}

	/**
	 * ������Ŀ¼��һ�����µ��ļ����������򼴴������¼�
	 * 
	 * @param wd
	 *            �����߳�id
	 * @param rootPath
	 *            ����Ŀ¼
	 * @param name
	 *            �ļ�����
	 */
	public void fileCreated(int wd, String rootPath, String name) {
		System.err.println("fileCreated, the created file path is " + rootPath + "/" + name);
	}

	public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
		System.err.println("fileRenamed, the old file path is " + rootPath + "/" + oldName
				+ ", and the new file path is " + rootPath + "/" + newName);
	}

	public void fileModified(int wd, String rootPath, String name) {
		System.err.println("fileModified, the modified file path is " + rootPath + "/" + name);
	}

	public void fileDeleted(int wd, String rootPath, String name) {
		System.err.println("fileDeleted , the deleted file path is " + rootPath + name);
	}
}
