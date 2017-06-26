package cn.mysession.core;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;

/**
 * 基于java.nio.file包实现的监听目录 另起一个线程开启服务
 * 
 * @author smith
 *
 */
public class NioShareDirectory2 implements ShareDirectory {

	// 监听目录映射集合
	private Map<WatchKey, File> dirMap = new HashMap<WatchKey, File>();
	// 处理方式集合
	private List<Process> processList = new ArrayList<Process>();
	// 监听服务
	private WatchService watcher = null;
	// 是否进行监听标识
	private boolean isRun = true;

	public NioShareDirectory2() {
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("监听服务获取失败.");
		}
	}

	@Override
	public List<File> addDir(File file) {
		List<File> exceptions = new ArrayList<File>();
		try {
			Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
System.out.println(dir);					
					
					WatchKey register = dir.register(watcher, ENTRY_CREATE);
//					WatchKey register = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
					dirMap.put(register, dir.toFile());
//dirMap.get(register).toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
//					System.out.println(exc);
					exceptions.add(file.toFile());
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(file + ": 目录注册监听失败.");
		}
		return exceptions;
	}

	@Override
	public void removeDir(File file) {
		WatchKey watchKey = null;
		ArrayList<WatchKey> all = new ArrayList<WatchKey>();
		Set<Entry<WatchKey, File>> set = dirMap.entrySet();
		Iterator<Entry<WatchKey, File>> it = set.iterator();
		while (it.hasNext()) {
			Entry<WatchKey, File> entry = it.next();
			if (entry.getValue().equals(file)) {
				watchKey = entry.getKey();
				all.add(watchKey);
				watchKey.cancel();
			}
		}
		dirMap.remove(all);
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
						WatchKey key = watcher.take();
						Path dir = dirMap.get(key).toPath();
						if (dir == null) {
							System.err.println("操作未识别");
							continue;
						}
						a: for (WatchEvent<?> event : key.pollEvents()) {
							Kind<?> kind = event.kind();
							// 事件可能丢失或遗弃
							if (kind == OVERFLOW) {
								continue;
							}
							// 目录内的变化可能是文件或者目录
							@SuppressWarnings("unchecked")
							WatchEvent<Path> ev = (WatchEvent<Path>) event;
							Path name = ev.context();
							Path child = dir.resolve(name);
							File file = child.toFile();
							for (Process process : processList) {
								switch (kind.name()) {
								case "ENTRY_CREATE":
									process.create(file);
									break;
								case "ENTRY_DELETE":
									process.delete(file);
									break;
								case "ENTRY_MODIFY":
									process.modify(file);
									break;
								default:
									continue a;
								}
							}
							if (kind == ENTRY_CREATE) {
								if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
									addDir(child.toFile());
								}
							}
							boolean valid = key.reset();
							if (!valid) {
								dirMap.remove(key);
								if (dirMap.isEmpty()) {
									break;
								}
							}
						}
					}
				} catch (ClosedWatchServiceException e) {
					System.err.println(e);
				} catch (InterruptedException e) {
					System.err.println(e);
					//.....
					return;
				}
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
