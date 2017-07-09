package zp.myhotfix.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MyHotFix {


    private Context context;
    private MyHotFix(Context context)
    {
        this.context=context;
    }

    public static MyHotFix create(Context context)
    {
        return  new MyHotFix(context);
    }

    /**
     * 合并dex
     * @param fixDexPath
     */
    private boolean mergeDexAndApply(File fixDexPath, File appDexFile) {
        try {
            //创建dex的optimize路径
            File optimizeDir = new File(fixDexPath.getAbsolutePath(), "optimize_dex");////dex的优化路径
            if (!optimizeDir.exists()) {
                optimizeDir.mkdir();
            }
            //加载自身Apk的dex，通过PathClassLoader
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            //找到dex并通过DexClassLoader去加载
            //dex文件路径，优化输出路径，null,父加载器
            DexClassLoader dexClassLoader = new DexClassLoader(appDexFile.getAbsolutePath(), optimizeDir.getAbsolutePath(), null, pathClassLoader);
            //获取app自身的BaseDexClassLoader中的pathList字段
            Object appDexPathList = ReflectUtil.getFieldValue(pathClassLoader,Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
            //获取补丁的BaseDexClassLoader中的pathList字段
            Object fixDexPathList = ReflectUtil.getFieldValue(dexClassLoader,Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");

            //获得pathList中的dexElements
            Object appDexElements = ReflectUtil.getFieldValue(appDexPathList,null,"dexElements");
            Object fixDexElements = ReflectUtil.getFieldValue(fixDexPathList,null,"dexElements");
            //合并两个elements的数据，将修复的dex插入到数组最前面
            Object finalElements = combineArray(fixDexElements, appDexElements);
            //给app 中的dex pathList 中的dexElements 重新赋值
            boolean isSuccess=ReflectUtil.setFiledValue(appDexPathList,"dexElements", finalElements);
            return isSuccess;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    /**
     * 两个DexElement数组合并
     *
     * @param fixDexElementArray 补丁dex元素数组
     * @param appDexElementsArray app dex元素数组
     * @return
     */
    private  Object combineArray(Object fixDexElementArray, Object appDexElementsArray) {
        Class<?> localClass = fixDexElementArray.getClass().getComponentType();
        int i = Array.getLength(fixDexElementArray);
        int j = i + Array.getLength(appDexElementsArray);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(fixDexElementArray, k));
            } else {
                Array.set(result, k, Array.get(appDexElementsArray, k - i));
            }
        }
        return result;
    }



    /**
     * 复制SD卡中的补丁文件到dex目录
     */
    public void loadPatch(final File fixDexFile,final FixCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fixDexPath="fix_dex";//fixDex存储的路径
                File dexFileDir = context.getDir(fixDexPath, Context.MODE_PRIVATE);
                File copyDexFile = new File(dexFileDir, fixDexFile.getName());
                if (copyDexFile.exists()) {
                   // copyDexFile.delete();
                    // TODO: 2017/7/8  本地文件是否一致，待验证
                    boolean isSuccess=mergeDexAndApply(dexFileDir,copyDexFile);
                    callBack.onPatchLoaded(isSuccess);
                    return;
                }
                //copy
                InputStream is = null;
                FileOutputStream os = null;
                try {
                    is = new FileInputStream(fixDexFile);
                    os = new FileOutputStream(copyDexFile);
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                    if (copyDexFile.exists()) {
                        //复制成功,进行修复

                        //mrege and fix
                        boolean isSuccess=mergeDexAndApply(dexFileDir,copyDexFile);
                        callBack.onPatchLoaded(isSuccess);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onPatchLoaded(false);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
    public interface FixCallBack
    {
        void onPatchLoaded(boolean isSucess);
    }
}