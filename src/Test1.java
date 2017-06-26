

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

import org.junit.Test;

import cn.mysession.core.NioShareDirectory2;
import cn.mysession.core.SystemShow;
import cn.mysession.myprocess.DeleteProcess;

public class Test1 {

	public static void main(String[] args) {
		
//		File file = new File("\\\\ERROR\\Users\\Public");
//		System.out.println(file.list().length);
		
		NioShareDirectory2 shareDirectory = new NioShareDirectory2();
//		shareDirectory.addDir(new File("D:\\"));
//		shareDirectory.addDir(new File("\\\\error\\Users"));
		//		shareDirectory.addDir(new File("\\\\ERROR\\Users\\Public\\Videos"));
//		shareDirectory.addProcess(new PrintProcess());
		DeleteProcess deleteProcess = new DeleteProcess();
		deleteProcess.setShow(new SystemShow());
		shareDirectory.addProcess(deleteProcess);
		System.out.println("局域网共享文件夹监听指挥中心----启动成功");
		shareDirectory.start();
		
		Scanner scanner = new Scanner(System.in);
		while(true) {
			shareDirectory.addDir(new File(scanner.nextLine()));
		}
	}
	
	@Test
	public void fun() {
		File file = new File("\\\\ERROR\\Users\\Public\\Videos\\Sample Videos");
		try {
			Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					System.out.println(dir);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					System.out.println(exc);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (AccessDeniedException e2) {
			System.err.println(e2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void fun2() {
		File file = new File("\\\\ERROR\\Users\\Public\\ZZZ\\新建 Microsoft Word 文档.docx");
		file.delete();
		System.out.println(file);
	}
}
