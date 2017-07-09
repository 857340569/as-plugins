package zp.myhotfix;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import zp.myhotfix.test.Test;
import zp.myhotfix.utils.HotFixEngine;
import zp.myhotfix.utils.TextUtils;
import zp.myhotfix.view.SpanableStringTextView;

public class SecondActivity extends Activity {
    private TextView show_info_view;
    private ListView listview;
    private String teststr="balabalaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaddddddddddddddddddbalabalaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaddddddddddddddddddddddddddddddd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
    }
    private void initViews(){
        show_info_view = findView(R.id.show_info_view);
        listview = findView(R.id.listview);
//        show_info_view.setText("I'm bugs!");
//        show_info_view.setText("I'm fixed! ok");
        show_info_view.setText("计算"+ Test.getResult(5,2));
        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView=LayoutInflater.from(SecondActivity.this).inflate(R.layout.item,null);
                SpanableStringTextView textView= (SpanableStringTextView) convertView.findViewById(R.id.tv_detail);
                SpannableStringBuilder style= TextUtils.getTextStyleBuilder(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(SecondActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        //super.updateDrawState(ds);
                        ds.setColor(Color.RED);
                        ds.setUnderlineText(false);
                    }
                },position+teststr,"bala");
                textView.setText(style);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                return convertView;
            }
        });
    }


    private <T extends View> T findView(int view_id) {
        return (T) findViewById(view_id);
    }
}
