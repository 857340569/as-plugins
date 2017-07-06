package zp.myhotfix;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import zp.myhotfix.utils.HotFixEngine;

public class MainActivity extends Activity {
    private Button btn_patch;
    private Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews(){
        btn_patch = findView(R.id.btn_patch);
        start_btn = findView(R.id.start_two);
        btn_patch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "没有找到补丁文件", Toast.LENGTH_SHORT).show();
                HotFixEngine.copyDexFileToAppAndFix(MainActivity.this,"classes_fix.dex",true);
            }
        });
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }


    private <T extends View> T findView(int view_id) {
        return (T) findViewById(view_id);
    }
}
