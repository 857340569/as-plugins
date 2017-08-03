package zp.tools.patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {
	
	/**
	 * 读取目录下所有文件
	 * 
	 * @param returnFiles 目录下文件集合<以指定目录下文件相对路径作为key,对应的File为value>
	 * @param path 指定的目录 
	 * @return
	 */
	public static Map<String, File> readPathFiles(Map<String, File> returnFiles, String path) {
		File pathDir = new File(path);
		if (returnFiles == null) {
			returnFiles = new HashMap<String, File>();
			returnFiles.put("parentPath", pathDir);
		}
		if (pathDir.exists() && pathDir.isDirectory()) {
			File[] files = pathDir.listFiles();
			for (File temp : files) {
				if (temp.isDirectory()) {
					readPathFiles(returnFiles, temp.getAbsolutePath());
					continue;
				}
				String key = temp.getAbsolutePath();
				if (!key.toLowerCase().endsWith(".class")) {
					continue;
				}
				File parentPath = returnFiles.get("parentPath");
				key = key.substring(parentPath.getAbsolutePath().length() + 1);
				returnFiles.put(key, temp);
			}
		}
		return returnFiles;
	}
	
	/**
	 * 找出两个目录下相应被修改过的文件，并复制到指定位置
	 * @param oldPath
	 * @param newPath
	 */
	public static void findDiff(String oldPath, String newPath,String patchPath) {
		// 找到被修改的文件，复制到指定目录中
		Map<String, File> oldClassFiles = readPathFiles(null, oldPath);
		Map<String, File> newClassFiles = readPathFiles(null, newPath);
		Iterator<String> keys=oldClassFiles.keySet().iterator();
		while(keys.hasNext())
		{
			String key=keys.next();
			File oldClassFile=oldClassFiles.get(key);
			File newClassFile=newClassFiles.get(key);
			if(oldClassFile.isDirectory()||newClassFile.isDirectory())
			{
				continue;
			}
			if(!isSameFile(oldClassFile, newClassFile))
			{
				FileUtils.copyFile(new File(newClassFile.getAbsolutePath()), patchPath+key);
			}
		}
	}

	
	
	/**
	 * 复制文件 
	 * @param srcFile 原文件 
	 * @param targetFilePath 目标位置
	 */
	public static void copyFile(File srcFile, String targetFilePath) {
		if (srcFile == null || !srcFile.exists())
			return;
		if (targetFilePath == null || targetFilePath.trim().equals(""))
			return;
		String fileName = srcFile.getName();
		if (!targetFilePath.endsWith(fileName)) {
			targetFilePath += fileName;
		}
//		System.out.println(targetFilePath);
		File outputFile = new File(targetFilePath);
		InputStream isStream = null;
		OutputStream oStream = null;
		try {
			File parentDir=new File(outputFile.getParent());
			if(!parentDir.exists()||parentDir.isFile())
			{
				parentDir.mkdirs();
			}
			isStream = new FileInputStream(srcFile);
			oStream = new FileOutputStream(outputFile);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = isStream.read(buff)) != -1) {
				oStream.write(buff, 0, len);
			}
			oStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("文件复制失败:" + targetFilePath);
		} finally {
			closeStream(isStream, oStream);
		}

	}
	
	
	/**
	 * 判断两个文件内容是否一样
	 * 
	 * @param firstFile
	 * @param secondFile
	 * @return
	 */
	public static boolean isSameFile(File firstFile, File secondFile) {
		if (firstFile == null || !firstFile.exists() || secondFile == null || !secondFile.exists())
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
			closeStream(fiStream, null);
			closeStream(siStream, null);
		}

		return false;
	}
	

	/**
	 * 删除指定目录下面所有文件及目录
	 * @param rootPath
	 */
	public static void deleteFiles(String rootPath)
	{
		if(rootPath==null||rootPath.trim().equals("")) return;
		File rootFile=new File(rootPath);
		if(!rootFile.exists())
		{
			return;
		}
		if(rootFile.isDirectory())
		{
			File[] files=rootFile.listFiles();
			if(files.length==0) 
			{
				rootFile.delete();
				return;
			}
			for(File temp:files)
			{
				deleteFiles(temp.getAbsolutePath());
			}
			
		}
		rootFile.delete();
	}
	
	/**
	 * 关闭输入、输出流
	 * 
	 * @param is
	 * @param os
	 */
	public static void closeStream(InputStream is, OutputStream os) {
		try {
			if (is != null)
				is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (os != null)
				os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
