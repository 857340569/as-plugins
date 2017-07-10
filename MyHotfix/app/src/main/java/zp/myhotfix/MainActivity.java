package zp.myhotfix;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView btn_patch;
    private TextView tipsView;
    private Button start_btn;
    private int times=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews(){
        btn_patch = findView(R.id.btn_patch);
        tipsView = findView(R.id.tips);
        start_btn = findView(R.id.start_two);
        btn_patch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "没有找到补丁文件", Toast.LENGTH_SHORT).show();
                //HotFixEngine.copyDexFileToAppAndFix(MainActivity.this,"classes_fix.dex",true);
                Animation animationEnter= AnimationUtils.loadAnimation(MainActivity.this,R.anim.tips_anim_enter);
                tipsView.setVisibility(View.VISIBLE);
                tipsView.startAnimation(animationEnter);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animationExit= AnimationUtils.loadAnimation(MainActivity.this,R.anim.tips_anim_exit);
                        tipsView.startAnimation(animationExit);
                        animationExit.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                tipsView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                },3000);
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
