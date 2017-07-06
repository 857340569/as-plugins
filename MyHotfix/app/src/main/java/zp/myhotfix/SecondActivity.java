package zp.myhotfix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import zp.myhotfix.utils.HotFixEngine;

public class SecondActivity extends Activity {
    private TextView show_info_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
    }
    private void initViews(){
        show_info_view = findView(R.id.show_info_view);
//        show_info_view.setText("I'm bugs!");
        show_info_view.setText("I'm fixed!");
    }


    private <T extends View> T findView(int view_id) {
        return (T) findViewById(view_id);
    }
}
