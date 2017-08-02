package zp.tools.patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtils {
	public static void main(String[] args) throws Exception {
		Map<String, File> returnFiles = readPathFiles(null, "D:\\workspace\\CreateAndroidPatch");
		System.out.println(returnFiles);
		System.out.println(isSameFile(returnFiles.get("D:\\workspace\\CreateAndroidPatch\\.classpath"),
				returnFiles.get("D:\\workspace\\CreateAndroidPatch\\.classpath")));
	}

	public static Map<String, File> readPathFiles(Map<String, File> returnFiles, String path) {
		if (returnFiles == null) {
			returnFiles = new HashMap<String, File>();
		}
		File pathDir = new File(path);
		if (pathDir.exists() && pathDir.isDirectory()) {
			File[] files = pathDir.listFiles();
			for (File temp : files) {
				if (temp.isDirectory()) {
					readPathFiles(returnFiles, temp.getAbsolutePath());
					continue;
				}
				System.out.println(temp.getPath());

				returnFiles.put(temp.getPath(), temp);
			}
		}
		return returnFiles;
	}

	public static boolean isSameFile(File firstFile, File secondFile) {
		if (firstFile == null||!firstFile.exists() || secondFile == null|| !secondFile.exists())
			return false;
		if (firstFile.length() != secondFile.length()) {
			return false;
		}
		InputStream fiStream = null;
		InputStream siStream = null;
		try {
			fiStream = new FileInputStream(firstFile);
			siStream = new FileInputStream(secondFile);
			int temp = 0;
			while ((temp = fiStream.read()) != -1) {
				if (temp != siStream.read()) {
					return false;
				}
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (fiStream != null)
					fiStream.close();
				if (siStream != null)
					siStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;

	}
}
