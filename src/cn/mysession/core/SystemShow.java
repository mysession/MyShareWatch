package cn.mysession.core;

/**
 * �ڿ���̨�������
 * @author smith
 *
 */
public class SystemShow implements Show {

	@Override
	public void setShow(String message) {
		System.out.println(message);
	}

}
