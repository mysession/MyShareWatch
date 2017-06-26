package cn.mysession.core;

import java.io.File;
import java.util.List;

/**
 * 监听注册目录接口
 * 为什么要个接口呢, 解耦
 * 只监听文件, 不监听文件夹
 * @author smith
 *
 */
public interface ShareDirectory {

	/**
	 * 添加要监听的目录
	 * @param file
	 * @return 返回没有版权监听的目录出错异常集合
	 */
	public List<File> addDir(File file);
	
	/**
	 * 移除已经监听的目录
	 * @param file
	 */
	public void removeDir(File file);
	
	/**
	 * 添加处理
	 * @param process
	 */
	public void addProcess(Process process);
	
	/**
	 * 移除处理
	 * @param process
	 */
	public void removeProcess(Process process);
	
	/**
	 * 开始监听
	 */
	public void start();
	
	/**
	 * 停止监听
	 */
	public void stop();
}
