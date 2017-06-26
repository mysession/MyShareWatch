package cn.mysession.lan;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供局域网相关操作的一些需要的方法
 * 
 * @author smith
 *
 */
public class Lan {

	/**
	 * 获取网络共享计算机名称
	 * 
	 * @param host
	 *            是否包括本台计算机的名称
	 * @return
	 */
	public static Set<String> getNeighbour(boolean host) {
		// 查看本地局域网中所有与本机通信的IP地址
		// arp -a
		// net view
		Set<String> neighbours = new HashSet<String>();
		String command = "net view";
		String cmd = cmd(command);
		Pattern p = Pattern.compile("\\\\\\\\.+\\s");
		Matcher m = p.matcher(cmd);
		while (m.find()) {
			// System.out.println(m.group(0).trim());
			String name = m.group(0).trim();
			String name2 = "\\\\" + getLocalHost();
			if (!(false == host && name2.equalsIgnoreCase(name))) {
				neighbours.add(name);
			}

		}

		return neighbours;
	}

	/**
	 * 获取指定计算机共享的文件夹
	 * @param name 计算机名
	 * @return 
	 */
	public static Set<String> getNeighbourDirectory(String name) {
		Set<String> directorys = new HashSet<String>();

		String command = "net view " + name;
		String cmd = cmd(command);
		
		Pattern p = Pattern.compile("\n(.+)Disk");
		Matcher m = p.matcher(cmd);
		while (m.find()) {
			String dir = m.group(1).trim();
			directorys.add(dir);
		}
		
		return directorys;
	}

	//cmd命令, 返回结果
	private static String cmd(String command) {
		StringBuilder builder = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process exec = runtime.exec(command);
			InputStream inputStream = exec.getInputStream();

			// 读取命令行结果
			byte[] b = new byte[1024];
			int length = -1;
			while ((length = inputStream.read(b)) != -1) {
				builder.append(new String(b, 0, length));
			}
			inputStream.close();
		} catch (IOException e) {
			System.out.println("net view 命令出错");
			e.printStackTrace();
		}
//System.out.println(builder.toString());
		return builder.toString();
	}

	/**
	 * 获取本机名称
	 * 
	 * @return
	 */
	public static String getLocalHost() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}
	
	/**
	 * 从共享目录中获取计算机名
	 * @param dir
	 * @return
	 */
	public static String getComputName(String dir) {
		Pattern p = Pattern.compile("\\\\\\\\(.+?)\\\\");
		Matcher m = p.matcher(dir);
		if (m.find()) {
//			System.out.println(m.group(1).trim());
			return m.group(1).trim();
		}
		return "";
	}
	
	/**
	 * 获取文件的名字
	 * @param dir
	 * @return
	 */
	public static String getFileName(String dir) {
		Pattern p = Pattern.compile(".+\\\\(.+)");
		Matcher m = p.matcher(dir);
		if (m.find()) {
//			System.out.println(m.group(1).trim());
			return m.group(1).trim();
		}
		return "";
	}
}
