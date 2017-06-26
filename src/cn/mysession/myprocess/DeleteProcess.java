package cn.mysession.myprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cn.mysession.core.MyDate;
import cn.mysession.core.Process;
import cn.mysession.core.Show;

/**
 * 将文件进行删除处理, 不删除目录
 * @author smith
 *
 */
public class DeleteProcess extends BaseProcess{

	private Show show = null;
	private List<File> deleteFile = new MyArrayList<File>();
	private List<File> deleteFileRemove = new ArrayList<File>();
	
	private DeleteThread deleteThread = new DeleteThread();
	
	private Object object = new Object();
	private ProcessType type = null;
	
	public DeleteProcess() {
		deleteThread.start();
	}
	
	@Override
	public void delete(File file) {
		deleteFile(file);
		type = ProcessType.DELETE;
	}

	@Override
	public void create(File file) {
		deleteFile(file);
		type = ProcessType.CREATE;
	}

	@Override
	public void modify(File file) {
		deleteFile(file);
		type = ProcessType.MODIFY;
	}

	private boolean deleteFile(File file) {
		return deleteFile.add(file);
	}
	
	//ArrayList代理类, 观察者
	class MyArrayList<E> extends ArrayList<E> {
		private static final long serialVersionUID = 6981493703408675577L;
		@Override
		public boolean add(E e) {
			boolean b = super.add(e);
			synchronized (object) {
				object.notifyAll();
			}/**/
			return b;
		}
		
	}
	
	//有东西的时候再删
	class DeleteThread extends Thread {

		@Override
		public void run() {
			while(true) {

				ArrayList<File> temp = new ArrayList<File>();
				temp.addAll(deleteFile);
				for(File file : temp) {
					boolean b = file.delete();
					if(true == b) {
						if(show != null) {
							show.setShow("程序删除成功 "+MyDate.getDate()+" - " + file);
							notifyNextProcess(file, type);
						}
						deleteFileRemove.add(file);
					}
				}
				deleteFile.removeAll(deleteFileRemove);
				deleteFileRemove.clear();
				if(deleteFile.size() == 0) {
					synchronized (object) {
						try {
							object.wait();
						} catch (InterruptedException e1) {}
					}/**/
//					break;
				}
			}
		}
		
	}

	@Override
	public void setShow(Show show) {
		this.show = show;
	}
}
