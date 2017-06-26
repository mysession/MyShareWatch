package cn.mysession.myprocess;

import java.io.File;

import cn.mysession.core.Process;
import cn.mysession.core.Show;

/**
 * 继承此类记得处理成功时notifyNextProcess
 * @author smith
 *
 */
public abstract class BaseProcess implements Process {

	private Process nextProcess = null;
	protected Show show = null;
	
	public enum ProcessType{  
		CREATE,DELETE,MODIFY  
	} 
	
	public boolean fileIsOk(File file) {
		return ProcessTools.isOK(file);
	}
	
	public void notifyNextProcess(File file, ProcessType type) {
		if(nextProcess==null)
			return;
		if(type == ProcessType.CREATE) {
			nextProcess.create(file);
		} else if (type == ProcessType.DELETE) {
			nextProcess.delete(file);
		} else if (type == ProcessType.DELETE) {
			nextProcess.modify(file);
		}
	}
	
	@Override
	public Process setNextProcess(Process nextprocess) {
		nextProcess = nextprocess;
		return nextprocess;
	}
	
	@Override
	public void setShow(Show show) {
		this.show = show;
	}
}
