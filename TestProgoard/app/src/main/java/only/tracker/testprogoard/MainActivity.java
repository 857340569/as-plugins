package only.tracker.testprogoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import only.tracker.testprogoard.utils.StringUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(StringUtils.isEmpty(""));
    }
}
