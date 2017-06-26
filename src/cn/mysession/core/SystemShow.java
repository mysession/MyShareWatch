package cn.mysession.core;

/**
 * 在控制台进行输出
 * @author smith
 *
 */
public class SystemShow implements Show {

	@Override
	public void setShow(String message) {
		System.out.println(message);
	}

}
