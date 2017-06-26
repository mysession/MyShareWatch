package cn.mysession.core;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ����java.nio.file��ʵ�ֵļ���Ŀ¼
 * ����һ���߳̿�������
 * @author smith
 *
 */
public class NioShareDirectory implements ShareDirectory {

	//����Ŀ¼ӳ�伯��
	private Map<File, WatchKey> dirMap = new HashMap<File, WatchKey>();
	//����ʽ����
	private List<Process> processList = new ArrayList<Process>();
	//��������
	private WatchService watcher = null;
	//�Ƿ���м�����ʶ
	private boolean isRun = true;
	
	public NioShareDirectory() {
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("���������ȡʧ��.");
		}
	}
	
	@Override
	public List<File> addDir(File file) {
		try {
			dirMap.put(file, file.toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(file+": Ŀ¼ע�����ʧ��.");
		}
		return null;
	}

	@Override
	public void removeDir(File file) {
		dirMap.get(file).cancel();
		dirMap.remove(file);
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
		new Thread() {
			@Override
			public void run() {
				try {
					while (true == isRun) {
						//�ȴ�֪ͨ
						WatchKey key = watcher.take();
						
						for (WatchEvent<?> event : key.pollEvents()) {
System.out.println("wait event");
							
							Kind<?> kind = event.kind();
							
			
							if (kind == OVERFLOW) {// �¼�����lost or discarded��ʧ����
								continue;
							}
			
							@SuppressWarnings("unchecked")
							WatchEvent<Path> e = (WatchEvent<Path>) event;
							Path filedir = e.context();
							
							for(Process process : processList) {
								process.create(filedir.toFile());
								
							}
							
							//�ڸ�Ŀ¼������ļ���, �ڸ�Ŀ¼������ļ�
							//����Ŀ¼������ļ���, ����Ŀ¼������ļ�
//							System.out.println(new Date());
//							System.out.printf("Event %s has happened,which fileName is %s%n\n", kind.name(), filedir.getFileName());
						}
						if (!key.reset()) {
							break;
						}
					}
				} catch (InterruptedException e1) {
					System.out.println("��ȡ֪ͨ�ж�.");
				} catch (ClosedWatchServiceException e) {}
			}
		}.start();
	}

	@Override
	public void stop() {
		isRun = false;
		try {
			watcher.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��������ر�ʧ��");
		}
	}

}
