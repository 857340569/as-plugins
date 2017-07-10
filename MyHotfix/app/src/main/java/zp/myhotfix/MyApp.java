package zp.myhotfix;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import zp.myhotfix.utils.MyHotFix;

/**
 * Created by tracker on 2017/7/8.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyHotFix.create(this).loadPatch(new File(Environment.getExternalStorageDirectory(), "fixed.dex"), new MyHotFix.FixCallBack() {
            @Override
            public void onPatchLoaded(boolean isSucess) {
                System.out.println("MyApp:"+isSucess);
            }
        });
    }
}
