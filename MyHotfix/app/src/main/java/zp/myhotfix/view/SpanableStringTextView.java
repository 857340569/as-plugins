package zp.myhotfix.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

/**
 * Created by LF on 2017/7/7.
 */

public class SpanableStringTextView extends AppCompatTextView {


    public SpanableStringTextView(Context context) {
        super(context);
    }

    public SpanableStringTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpanableStringTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 注意：spannableString 设置Spannable 的对象到spannableString中时，要用Spannable.SPAN_EXCLUSIVE_EXCLUSIVE的flag值，不然可能会会出现后面的衔接字符串不会显示
     */
    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence charSequence = getText();
//        int lastCharDown = getLayout().getLineVisibleEnd(getLineCount()-1);
//        System.out.println(getLineCount()+" "+charSequence.length()+" "+lastCharDown);
        //&&charSequence.length() > lastCharDown
        if (getLineCount()>=4) {
            int lastCharDown = getLayout().getLineVisibleEnd(3);
            if(charSequence.length() > lastCharDown)
            {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append(charSequence.subSequence(0, lastCharDown - 4)).append("...");
                setText(spannableStringBuilder);
            }
        }
        super.onDraw(canvas);
    }
}
