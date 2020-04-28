package net.mczmmj.ormframework.core;

/**
 *    负责java类型和数据库类型的相互转换　
 * @author xiaoming
 */

public interface TypeConvertor {

    /**
     * 将数据库类型转换为java类型
     * @param columnType 数据库字段类型
     * @return java类型
     */
    public String databaseType2JavaType(String columnType);

    /**
     * 将java类型转换为数据库类型
     * @param javaType java类型
     * @return 数据库类型
     */
    public String javaType2DatabaseType(String  javaType);

}
