package cn.mysession.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;

public class JnotifyShareDirectory implements ShareDirectory {

	//未添加监听目录集合
	private List<File> noAddFiles = new ArrayList<File>();
	private Map<File, Integer> files = new HashMap<File, Integer>();
	// 处理方式集合
	private List<Process> processList = new ArrayList<Process>();
	//监听选项
	private int mask = JNotify.FILE_CREATED | JNotify.FILE_RENAMED;
	//是否监听子目录
	private boolean watchSubtree = true;
	private JNotifyAdapter listener = new MyJnotifyListener();
	
	//等待锁
	private Object object = new Object();
	
	@Override
	public List<File> addDir(File file) {
		noAddFiles.add(file);
		try {
			int watchID = JNotify.addWatch(file.getPath(), mask, watchSubtree, listener);
			files.put(file, watchID);
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void removeDir(File file) {
		try {
			JNotify.removeWatch(files.get(file));
			files.remove(file);
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addProcess(Process process) {
		processList.add(process);
	}

	@Override
	public void removeProcess(Process process) {
		processList.remove(process);
	}

	@Override
	public void start() {
		try {
			/*for(File file : noAddFiles) {
					int watchID = JNotify.addWatch(file.getPath(), mask, watchSubtree, listener);
					files.put(file, watchID);
			}*/
			noAddFiles.clear();
			synchronized (object) {
				object.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		synchronized (object) {
			object.notifyAll();
		}
	}

	class MyJnotifyListener extends JNotifyAdapter {
		
		@Override
		public void fileCreated(int wd, String rootPath, String name) {
			File file = new File(rootPath + File.separator + name);
			if(file.isDirectory())
				return;
			for(Process process : processList) {
				process.create(file);
			}
		}
		
		@Override
		public void fileDeleted(int wd, String rootPath, String name) {
			File file = new File(rootPath + File.separator + name);
			if(file.isDirectory())
				return;
			for(Process process : processList) {
				process.delete(file);
			}
		}
		
		@Override
		public void fileModified(int wd, String rootPath, String name) {
			File file = new File(rootPath + File.separator + name);
			if(file.isDirectory())
				return;
			for(Process process : processList) {
				process.modify(file);
			}
		}
		
		@Override
		public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
			fileModified(wd, rootPath, newName); 
		}
		
	}
}