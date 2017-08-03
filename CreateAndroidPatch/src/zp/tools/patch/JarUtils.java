package zp.tools.patch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarUtils {
	private static final String BASE_PATH = System.getProperty("user.dir") + "\\";
	private static final String OLD_APK_PATH = BASE_PATH + "apk\\oldapk\\";
	private static final String NEW_APK_PATH = BASE_PATH + "apk\\newapk\\";
	// 生成补丁的位置
	private static final String PATCH_PATH = BASE_PATH + "patch\\";
	private static final String TEMP_PATH = BASE_PATH + "temp\\";
	private static final String TEMP_OLD_APK_PATH = TEMP_PATH + "oldapk\\";
	private static final String TEMP_NEW_APK_PATH = TEMP_PATH + "newapk\\";
	private static final String TEMP_OLD_CLASSES_PATH = TEMP_PATH + "oldapk\\classes\\";
	private static final String TEMP_NEW_CLASSES_PATH = TEMP_PATH + "newapk\\classes\\";
	private static final String TEMP_PATCH_CLASSES_PATH = TEMP_PATH + "patch\\classes\\";
	static {
		File oldApkPath=new File(OLD_APK_PATH);
		File newApkPath=new File(NEW_APK_PATH);
		if(!oldApkPath.exists()) oldApkPath.mkdirs();
		if(!newApkPath.exists()) newApkPath.mkdirs();
	}

	public static void main(String[] args) throws Exception {
//		
		// System.out.println(isSameFile(returnFiles.get(".svn\\wc.db-journal"),
		// returnFiles.get(".svn\\wc.db-journal")));
		// apkToJar(path+"\\SFLS.apk");
//		
//		copyFile(new File(OLD_APK_PATH+"SFLS.apk"), TEMP_OLD_APK_PATH);
//		copyFile(new File(NEW_APK_PATH+"SFLS.apk"), TEMP_NEW_APK_PATH);
//		waiting(5);
//		apkToJar(TEMP_OLD_APK_PATH+"SFLS.apk");
//		apkToJar(TEMP_NEW_APK_PATH+"SFLS.apk");
//		waiting(15);
//		decompressJar(TEMP_OLD_APK_PATH+"SFLS-dex2jar.jar", TEMP_OLD_CLASSES_PATH);
//		decompressJar(TEMP_NEW_APK_PATH+"SFLS-dex2jar.jar", TEMP_NEW_CLASSES_PATH);
		
//		findDiff(TEMP_OLD_CLASSES_PATH,TEMP_NEW_CLASSES_PATH,TEMP_PATCH_CLASSES_PATH);
		createPatch(TEMP_PATCH_CLASSES_PATH, "sfls_patch.dex",PATCH_PATH);
		//step1:将apk生成jar包
		//step2:解压jar包到指定位置
		//step3:找到所有已修改过的class文件
		//step4:生成补丁
		FileUtils.deleteFiles(TEMP_PATH);
	}

	


	/**
	 *
	 * 将apk生成jar包
	 * @param apkPath
	 *            apk 完整路径
	 */
	private static void apkToJar(String apkPath) {
		try {
			//exec(param3,param3,param3) param3 指定目录中执行命令
			Runtime.getRuntime().exec("cmd.exe /c d2j-dex2jar " + apkPath,null,new File(apkPath.substring(0,apkPath.lastIndexOf("\\"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * step2:解压jar包到指定位置
	 * @param jarPath
	 * @param outputPath
	 */
	private static void decompressJar(String jarPath, String outputPath) {
		if (jarPath == null || jarPath.trim().equals("") || !jarPath.toLowerCase().endsWith(".jar"))
			return;
		File jarFile = new File(jarPath);
		if (!jarFile.exists() || !jarFile.isFile())
			return;
		try {
			ZipFile zipFile = new ZipFile(jarFile);
			Enumeration<? extends ZipEntry> files = zipFile.entries();
			ZipEntry entry = null;
			File outFile = null;
			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;
			while (files.hasMoreElements()) {
				entry = files.nextElement();
				outFile = new File(outputPath+"\\"+entry.getName());
				// 如果条目为目录，则跳向下一个
				if (entry.isDirectory()) {
					outFile.mkdirs();
					continue;
				}
				try {
					bin = new BufferedInputStream(zipFile.getInputStream(entry));
					bout = new BufferedOutputStream(new FileOutputStream(outFile));
					byte[] buffer = new byte[1024];
					int readCount = -1;
					while ((readCount = bin.read(buffer)) != -1) {
						bout.write(buffer, 0, readCount);
					}
					bout.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {

				}
			}
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

	/**
	 * 生成补丁包
	 * 
	 * @param patchPath
	 *            打补丁的路径
	 * @param patchName
	 *            补丁名称
	 */
	private static void createPatch(String patchClassesPath, String patchName,String patchOutputPath) {
		if (patchName == null || patchName.trim().equals("")) {
			patchName = "patch_" + getTimestamp() + ".dex";
		}

		patchName = patchName.endsWith(".dex") ? patchName : patchName + ".dex";

		try {
			File outputDir=new File(patchOutputPath);
			if(!outputDir.exists())
			{
				outputDir.mkdirs();
			}
			String cmd="cmd.exe /c dx --dex --output=" + patchOutputPath + patchName + " " + patchClassesPath;
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 时间戳
	 * 
	 * @return
	 */
	private static String getTimestamp() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
	}

	
	private static void waiting(int times)
	{
		try {
			Thread.sleep(times*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}