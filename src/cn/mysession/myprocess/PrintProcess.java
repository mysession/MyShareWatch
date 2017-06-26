package cn.mysession.myprocess;

import java.io.File;

import cn.mysession.core.Process;
import cn.mysession.core.Show;
import cn.mysession.core.SystemShow;

/**
 * ��������Ϣ��ӡ���� 
 * @author smith
 *
 */
public class PrintProcess extends BaseProcess {

	private Show show = null;
	
	
	
	public PrintProcess() {
		this.show = new SystemShow();
	}

	@Override
	public void delete(File file) {
		show.setShow("ɾ��: " + file);
		notifyNextProcess(file, ProcessType.DELETE);
	}

	@Override
	public void create(File file) {
		show.setShow("����: " + file);
		notifyNextProcess(file, ProcessType.CREATE);
	}

	@Override
	public void modify(File file) {
		show.setShow("�޸�: " + file);
		notifyNextProcess(file, ProcessType.MODIFY);
 	}

	@Override
	public void setShow(Show show) {
		this.show = show;
	}

}
