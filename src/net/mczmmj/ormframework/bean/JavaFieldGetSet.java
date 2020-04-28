package net.mczmmj.ormframework.bean;

/**
 * 封装了java属性和set/get源码信息
 *
 * @author xiaoming
 *
 */
public class JavaFieldGetSet {
    /**
     * 属性的源码信息
     */
    private String fieldInfo;
    /**
     * get的源码信息
     */
    private String getInfo;
    /**
     * set的源码信息
     */
    private String setInfo;

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    public JavaFieldGetSet(String fieldInfo, String getInfo,
                           String setInfo) {
        super();
        this.fieldInfo = fieldInfo;
        this.getInfo = getInfo;
        this.setInfo = setInfo;
    }

    public JavaFieldGetSet() {
        super();
    }

    @Override
    public String toString() {
        System.out.println(fieldInfo);
        System.out.println(getInfo);
        System.out.println(setInfo);
        return super.toString();
    }

}
