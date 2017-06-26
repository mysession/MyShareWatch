package cn.mysession.myprocess;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import cn.mysession.core.MyDate;
import cn.mysession.core.Process;
import cn.mysession.core.Show;
import cn.mysession.lan.Lan;

/**
 *当发生修改时就将文件进行copy到指定文件夹
 * @author Administrator
 *
 */
public class CopyProcess extends BaseProcess {

	//
	private File targetDir = null;
	
	public CopyProcess(File dir) {
		this.targetDir = dir;
	}
	
	public CopyProcess(){}
	
	@Override
	public void delete(File file) {
//		copy(file, ProcessType.DELETE);
	}

	@Override
	public void create(File file) {
		copy(file, ProcessType.CREATE);
	}

	@Override
	public void modify(File file) {
//System.out.println("copy ok modify");
		copy(file, ProcessType.MODIFY);
	}

	private boolean copy(File file, ProcessType type) {
		if(!file.isFile())
			return false;
		new Thread() {
			@Override
			public void run() {
				try {
					while(!ProcessTools.isOK(file)) {
						Thread.sleep(500);
					}
					String name = Lan.getComputName(file.toString());
					Files.copy(file.toPath(), targetDir.toPath().resolve(name +" - "+ file.getName()), StandardCopyOption.REPLACE_EXISTING);
					show.setShow("程序复制成功 "+MyDate.getDate()+" - " + file);
					notifyNextProcess(file, type);
				} catch (Exception e) {}
			}
		}.start();
		return true;
	}

	public File getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}

	
}
