package cn.mysession.core;

import java.io.File;
import java.util.List;

/**
 * ����ע��Ŀ¼�ӿ�
 * ΪʲôҪ���ӿ���, ����
 * ֻ�����ļ�, �������ļ���
 * @author smith
 *
 */
public interface ShareDirectory {

	/**
	 * ���Ҫ������Ŀ¼
	 * @param file
	 * @return ����û�а�Ȩ������Ŀ¼�����쳣����
	 */
	public List<File> addDir(File file);
	
	/**
	 * �Ƴ��Ѿ�������Ŀ¼
	 * @param file
	 */
	public void removeDir(File file);
	
	/**
	 * ��Ӵ���
	 * @param process
	 */
	public void addProcess(Process process);
	
	/**
	 * �Ƴ�����
	 * @param process
	 */
	public void removeProcess(Process process);
	
	/**
	 * ��ʼ����
	 */
	public void start();
	
	/**
	 * ֹͣ����
	 */
	public void stop();
}
