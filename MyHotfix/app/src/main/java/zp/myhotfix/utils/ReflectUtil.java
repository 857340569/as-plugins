package zp.myhotfix.utils;

import java.lang.reflect.Field;

/**
 * Created by tracker on 2017/7/7.
 */

public class ReflectUtil {

    /**
     * 得到指定对象成员变量的值
     *
     * @param obj       指定的对象
     * @param claz      对象的类型
     * @param fieldName 成员变量名字
     * @return
     */
    public static Object getFieldValue(Object obj, Class<?> claz, String fieldName) {
        try {
            if (claz == null)
                claz = obj.getClass();
            Field field = claz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为指定对象中的字段重新赋值
     *
     * @param obj
     * @param filed
     * @param value
     */
    public static  boolean setFiledValue(Object obj, String filed, Object value) {
        try {
            Class<?> claz = obj.getClass();
            Field field = claz.getDeclaredField(filed);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }
}
