package net.mczmmj.ormframework.bean;


/**
 * 封装表中一个字段的信息
 *
 * @author xiaoming
 */
public class ColumnInfo {


    public static final int NORMAL_KEY = 0;
    public static final int PRIMARY_KEY = 1;
    public static final int FOREIGN_KEY = 2;

    /**
     * 字段名称
     */
    private String colName;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 字段的键类型
     * ０:普通键 1:主键 2:外键
     */
    private int keyType;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public ColumnInfo(String colName, String dataType, int keyType) {
        super();
        this.colName = colName;
        this.dataType = dataType;
        this.keyType = keyType;
    }

    public ColumnInfo() {
    }

}
