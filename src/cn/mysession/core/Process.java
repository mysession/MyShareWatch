package cn.mysession.core;

import java.io.File;

/**
 * 处理接口
 * 用于ShareDirectory监听目录接口 监听到事件时的处理接口
 * @author smith
 *
 */
public interface Process {

	/**
	 * 删除文件或文件夹时调用
	 * @param file 被删除的文件或文件夹
	 * @return 是否处理成功
	 */
	public void delete(File file);
	
	/**
	 * 创建文件或文件夹时调用
	 * @param file 被创建的文件或文件夹
	 * @return 是否处理成功
	 */
	public void create(File file);
	
	/**
	 * 修改文件或文件夹时调用
	 * @param file 被修改的文件或文件夹
	 * @return 是否处理成功
	 */
	public void modify(File file);
	
	/**
	 * 输出处理时出现的信息, 不设置为空不处理
	 * @param show
	 */
	public void setShow(Show show);
	
	/**
	 * 设置下一个处理的Process, 当这个处理完成时会开始处理下一个
	 * @param nextProcess
	 * @return 返回传入的Process
	 */
	public Process setNextProcess(Process nextProcess);
}
