
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;

/**
 * 
 * @author zxn 2014-08-10
 */
public class JnotifyDemo extends JNotifyAdapter {
	private static final String REQUEST_BASE_PATH = "\\\\ERROR\\Users";
	/** 被监视的目录 */
	String path = REQUEST_BASE_PATH;
	/** 关注目录的事件 */
//	int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
	int mask = JNotify.FILE_CREATED | JNotify.FILE_RENAMED;
	/** 是否监视子目录，即级联监视 */
	boolean watchSubtree = true;
	/** 监听程序Id */
	public int watchID;

	public static void main(String[] args) {
		new JnotifyDemo().beginWatch();
	}

	/**
	 * 容器启动时启动监视程序
	 * 
	 * @return
	 */
	public void beginWatch() {
		/** 添加到监视队列中 */
		try {
			this.watchID = JNotify.addWatch(path, mask, watchSubtree, this);
			System.err.println("jnotify -----------启动成功-----------");
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
		// 死循环，线程一直执行，休眠一分钟后继续执行，主要是为了让主线程一直执行
		// 休眠时间和监测文件发生的效率无关（就是说不是监视目录文件改变一分钟后才监测到，监测几乎是实时的，调用本地系统库）
		while (true) {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {// ignore it
			}
		}
	}

	/**
	 * 当监听目录下一旦有新的文件被创建，则即触发该事件
	 * 
	 * @param wd
	 *            监听线程id
	 * @param rootPath
	 *            监听目录
	 * @param name
	 *            文件名称
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
