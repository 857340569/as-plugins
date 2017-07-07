package zp.myhotfix.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;


import static android.text.TextUtils.isEmpty;

/**
 * Created by tracker on 2017/6/30.
 */

public class TextUtils {
    /**
     * 获取字体局部颜色样式
     * @param textColor 要设置的颜色
     * @param srcStr  要处理的文字
     * @param specialStr  要显示特殊颜色的文字
     * @return
     */
    public static SpannableStringBuilder getTextStyleBuilder(int textColor, String srcStr, String specialStr)
    {
        return getTextStyleBuilder(new ForegroundColorSpan(textColor),srcStr,specialStr);
    }

    /**
     * 获取字体局部颜色样式
     * @param span 样式
     * @param srcStr
     * @param specialStr
     * @param <T>
     * @return
     */
    public static <T> SpannableStringBuilder getTextStyleBuilder(T span, String srcStr, String specialStr)
    {
        SpannableStringBuilder style=new SpannableStringBuilder(srcStr);
        if(isEmpty(specialStr)||!srcStr.contains(specialStr))
        {
            return  style;
        }
        int startIndex=srcStr.indexOf(specialStr);
        style.setSpan(span,startIndex,startIndex+specialStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }
}
