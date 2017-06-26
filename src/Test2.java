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
		System.out.println("�����ʵ�������繲�������----��ʼ����");
		System.out.println("������������Ŀ¼,��ȴ�...;");
		// Set<String> neighbour = Lan.getNeighbour(false);
		// Set<String> dirs = null;
		// System.out.println(neighbour.toString());
		// System.out.println("��ȡ���, ��ʼע��Ŀ¼;");
		// unc·��
		ShareDirectory shareDirectory = new JnotifyShareDirectory();
/**/
//------------------
		SystemShow systemShow = new SystemShow();
//		CopyProcess copyProcess = new CopyProcess(new File("C:\\Users\\Administrator\\Desktop\\123"));
		File getFileDir = new File("���ص��ļ�������"); 
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
			// ��ȡ�������������
			Set<String> newNeighbour = Lan.getNeighbour(true);
			for (String name : newNeighbour) {
				Set<String> dirSet = neighbourDir.get(name);
				if(dirSet==null) {
					System.out.println("����:" + name);
					dirSet = new HashSet<String>();
					neighbourDir.put(name, dirSet);
				}
				// ��ȡ����Ŀ¼
				Set<String> directory = Lan.getNeighbourDirectory(name);
				
				// ��ӳɹ���ע��
				for (String dir : directory) {
					if (dirSet.add(dir)) {
						File file = new File(name + File.separator + dir);
						System.out.println("\t��ʼ����: " + file);
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
