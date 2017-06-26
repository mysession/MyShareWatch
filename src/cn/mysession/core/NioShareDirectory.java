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
 * 基于java.nio.file包实现的监听目录
 * 另起一个线程开启服务
 * @author smith
 *
 */
public class NioShareDirectory implements ShareDirectory {

	//监听目录映射集合
	private Map<File, WatchKey> dirMap = new HashMap<File, WatchKey>();
	//处理方式集合
	private List<Process> processList = new ArrayList<Process>();
	//监听服务
	private WatchService watcher = null;
	//是否进行监听标识
	private boolean isRun = true;
	
	public NioShareDirectory() {
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("监听服务获取失败.");
		}
	}
	
	@Override
	public List<File> addDir(File file) {
		try {
			dirMap.put(file, file.toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(file+": 目录注册监听失败.");
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
						//等待通知
						WatchKey key = watcher.take();
						
						for (WatchEvent<?> event : key.pollEvents()) {
System.out.println("wait event");
							
							Kind<?> kind = event.kind();
							
			
							if (kind == OVERFLOW) {// 事件可能lost or discarded丢失或丢弃
								continue;
							}
			
							@SuppressWarnings("unchecked")
							WatchEvent<Path> e = (WatchEvent<Path>) event;
							Path filedir = e.context();
							
							for(Process process : processList) {
								process.create(filedir.toFile());
								
							}
							
							//在根目录下添加文件夹, 在根目录下添加文件
							//在子目录下添加文件夹, 在子目录下添加文件
//							System.out.println(new Date());
//							System.out.printf("Event %s has happened,which fileName is %s%n\n", kind.name(), filedir.getFileName());
						}
						if (!key.reset()) {
							break;
						}
					}
				} catch (InterruptedException e1) {
					System.out.println("获取通知中断.");
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
			System.out.println("监听服务关闭失败");
		}
	}

}
