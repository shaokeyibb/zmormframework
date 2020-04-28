package net.mczmmj.ormframework.util;


import java.lang.reflect.Method;

/**
 * 封装反射的常用操作
 * @author xiaoming
 *
 */
public class ReflectUtil {

    /**
     * 调用obj对象对应属性的get方法
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object invokeGet(String fieldName, Object obj){
        //通过反射机制，调用属性的get方法
        try {
            Class c=obj.getClass();
            Method m=c.getDeclaredMethod("get"+StringUtil.firstChar2UpperCase(fieldName), null);
            return m.invoke(obj, null);
        } catch (Exception  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用obj对象对应属性的set方法
     * @param c
     * @param fieldName
     * @param obj
     */
    public static void  invokeSet( Object obj ,String fieldName,Object fieldValue){
        //通过反射机制，调用属性的set方法
        try {
            Class c=obj.getClass();
            Method m=c.getDeclaredMethod("set"+StringUtil.firstChar2UpperCase(fieldName), fieldValue.getClass());
            m.invoke(obj, fieldValue);
        } catch (Exception  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
