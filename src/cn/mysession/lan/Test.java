package cn.mysession.lan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	@org.junit.Test
	public void fun() {
		//��ȡ����ip�͵�ַ
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			System.out.println(localHost);
			String ip = localHost.getHostName();
			System.out.println(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@org.junit.Test
	public void fun2() {
		//�鿴���ؾ������������뱾��ͨ�ŵ�IP��ַ
		//arp -a
		//net view
		Runtime runtime = Runtime.getRuntime();
		String command = "net view";
		try {
			Process exec = runtime.exec(command);
			InputStream inputStream = exec.getInputStream();
			
			//��ȡ�����н��
			StringBuilder builder = new StringBuilder();
			byte[] b = new byte[1024];
			int length = -1;
			while((length = inputStream.read(b))!= -1) {
				builder.append(new String(b, 0, length));
			}
			System.out.println(builder.toString());
			//�ҳ��������
			Pattern p = Pattern.compile("\\\\\\\\.+\\s");
			Matcher m = p.matcher(builder.toString());
			while(m.find()) {
				System.out.println(m.group(0).trim());
			}
		} catch (IOException e) {
			System.out.println("net view �������");
			e.printStackTrace();
		}
	}
	
	@org.junit.Test
	public void fun3() {
		File file = new File("\\\\192.168.253.7");
		System.out.println(file.list());
		
		Path path = Paths.get("\\\\192.168.253.7\\*");
		System.out.println(path.toFile().list());
	}
	
	@org.junit.Test
	public void fun4() {
		Set<String> neighbour = Lan.getNeighbour(true);
		for(String s : neighbour) {
			System.out.println(s);
		}
	}
	
	@org.junit.Test
	public void fun5() {
		Lan.getNeighbourDirectory("error");
	}
}
