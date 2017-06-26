package cn.mysession.core;

import java.io.File;

/**
 * ����ӿ�
 * ����ShareDirectory����Ŀ¼�ӿ� �������¼�ʱ�Ĵ���ӿ�
 * @author smith
 *
 */
public interface Process {

	/**
	 * ɾ���ļ����ļ���ʱ����
	 * @param file ��ɾ�����ļ����ļ���
	 * @return �Ƿ���ɹ�
	 */
	public void delete(File file);
	
	/**
	 * �����ļ����ļ���ʱ����
	 * @param file ���������ļ����ļ���
	 * @return �Ƿ���ɹ�
	 */
	public void create(File file);
	
	/**
	 * �޸��ļ����ļ���ʱ����
	 * @param file ���޸ĵ��ļ����ļ���
	 * @return �Ƿ���ɹ�
	 */
	public void modify(File file);
	
	/**
	 * �������ʱ���ֵ���Ϣ, ������Ϊ�ղ�����
	 * @param show
	 */
	public void setShow(Show show);
	
	/**
	 * ������һ�������Process, ������������ʱ�Ὺʼ������һ��
	 * @param nextProcess
	 * @return ���ش����Process
	 */
	public Process setNextProcess(Process nextProcess);
}
