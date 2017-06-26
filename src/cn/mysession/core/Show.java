package cn.mysession.core;

/**
 * 此接口用于想将程序运行时的信息进行显示, 一般用于给用户提示
 * @author smith
 *
 */
public interface Show {

	/**
	 * 可以将程序运行时的信息, 状态进行显示
	 * @param message 程序运行时产生的信息
	 */
	public void setShow(String message);
}
