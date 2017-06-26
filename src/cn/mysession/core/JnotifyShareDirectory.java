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

	//δ��Ӽ���Ŀ¼����
	private List<File> noAddFiles = new ArrayList<File>();
	private Map<File, Integer> files = new HashMap<File, Integer>();
	// ����ʽ����
	private List<Process> processList = new ArrayList<Process>();
	//����ѡ��
	private int mask = JNotify.FILE_CREATED | JNotify.FILE_RENAMED;
	//�Ƿ������Ŀ¼
	private boolean watchSubtree = true;
	private JNotifyAdapter listener = new MyJnotifyListener();
	
	//�ȴ���
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