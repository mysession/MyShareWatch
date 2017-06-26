package cn.mysession.myprocess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.mysession.lan.Lan;

public class LogProcess extends BaseProcess {
	
	private static FileWriter writer = null;

	@Override
	public void delete(File file) {
		log(file);
	}

	@Override
	public void create(File file) {
		log(file);
	}

	@Override
	public void modify(File file) {
		log(file);
	}
	
	private void log(File file) {
		makeFile();
		try {
			Lan.getComputName(file.toString());
			writer.write(Lan.getComputName(file.toString()) + " \t 共享 \t " + file.getName() + "\r\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public File makeFile() {
		File dir = new File("共享日志"); 
		if(!dir.exists())
			dir.mkdirs();
		
		String format = new SimpleDateFormat("yyyy年MM月").format(new Date());
		
		File dir2 = new File(dir, format); 
		if(!dir2.exists())
			dir2.mkdirs();
		
		String format2 = new SimpleDateFormat("dd日").format(new Date());
		format2+=".txt";
		File dir3 = new File(dir2, format2); 
		if(!dir3.exists())
			try {
				dir3.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		if(writer == null) {
			try {
				writer = new FileWriter(dir3, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dir3;
	}

}
