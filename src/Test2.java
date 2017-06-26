import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.mysession.core.JnotifyShareDirectory;
import cn.mysession.core.Process;
import cn.mysession.core.ShareDirectory;
import cn.mysession.core.SystemShow;
import cn.mysession.lan.Lan;
import cn.mysession.myprocess.CopyProcess;
import cn.mysession.myprocess.DeleteProcess;
import cn.mysession.myprocess.LogProcess;

public class Test2 {

	public static void main(String[] args) {
		System.out.println("计算机实验室网络共享监听器----开始启动");
		System.out.println("正在搜索共享目录,请等待...;");
		// Set<String> neighbour = Lan.getNeighbour(false);
		// Set<String> dirs = null;
		// System.out.println(neighbour.toString());
		// System.out.println("获取完成, 开始注册目录;");
		// unc路径
		ShareDirectory shareDirectory = new JnotifyShareDirectory();
/**/
//------------------
		SystemShow systemShow = new SystemShow();
//		CopyProcess copyProcess = new CopyProcess(new File("C:\\Users\\Administrator\\Desktop\\123"));
		File getFileDir = new File("拦截的文件在这里"); 
		if(!getFileDir.exists())
			getFileDir.mkdir();
		CopyProcess copyProcess = new CopyProcess(getFileDir);
		copyProcess.setShow(systemShow);
		DeleteProcess deleteProcess = new DeleteProcess();
		deleteProcess.setShow(systemShow);
		copyProcess.setNextProcess(deleteProcess);
		deleteProcess.setNextProcess(new LogProcess());
		
		shareDirectory.addProcess(copyProcess);
//--------------------
 
//		DeleteProcess deleteProcess = new DeleteProcess();
//		deleteProcess.setShow(new SystemShow());
//		shareDirectory.addProcess(deleteProcess);
		
//		CopyProcess copyProcess = new CopyProcess(new File("f:"));
//		copyProcess.setShow(new SystemShow());
//		shareDirectory.addProcess(copyProcess);
		
		
		new AddDirThread(shareDirectory).start();

		shareDirectory.start();
	}

}

class AddDirThread extends Thread {
	private ShareDirectory shareDirectory = null;

	public AddDirThread(ShareDirectory shareDirectory) {
		this.shareDirectory = shareDirectory;
	}

	@Override
	public void run() {
		Map<String, Set<String>> neighbourDir = new HashMap<String, Set<String>>();
		while (true) {
			// 获取局域网计算机名
			Set<String> newNeighbour = Lan.getNeighbour(true);
			for (String name : newNeighbour) {
				Set<String> dirSet = neighbourDir.get(name);
				if(dirSet==null) {
					System.out.println("发现:" + name);
					dirSet = new HashSet<String>();
					neighbourDir.put(name, dirSet);
				}
				// 获取共享目录
				Set<String> directory = Lan.getNeighbourDirectory(name);
				
				// 添加成功就注册
				for (String dir : directory) {
					if (dirSet.add(dir)) {
						File file = new File(name + File.separator + dir);
						System.out.println("\t开始监听: " + file);
						shareDirectory.addDir(file);
					}

				}
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
