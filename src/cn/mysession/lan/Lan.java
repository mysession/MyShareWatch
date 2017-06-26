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
 * �ṩ��������ز�����һЩ��Ҫ�ķ���
 * 
 * @author smith
 *
 */
public class Lan {

	/**
	 * ��ȡ���繲����������
	 * 
	 * @param host
	 *            �Ƿ������̨�����������
	 * @return
	 */
	public static Set<String> getNeighbour(boolean host) {
		// �鿴���ؾ������������뱾��ͨ�ŵ�IP��ַ
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
	 * ��ȡָ�������������ļ���
	 * @param name �������
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

	//cmd����, ���ؽ��
	private static String cmd(String command) {
		StringBuilder builder = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process exec = runtime.exec(command);
			InputStream inputStream = exec.getInputStream();

			// ��ȡ�����н��
			byte[] b = new byte[1024];
			int length = -1;
			while ((length = inputStream.read(b)) != -1) {
				builder.append(new String(b, 0, length));
			}
			inputStream.close();
		} catch (IOException e) {
			System.out.println("net view �������");
			e.printStackTrace();
		}
//System.out.println(builder.toString());
		return builder.toString();
	}

	/**
	 * ��ȡ��������
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
	 * �ӹ���Ŀ¼�л�ȡ�������
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
	 * ��ȡ�ļ�������
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
