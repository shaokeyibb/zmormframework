package net.mczmmj.ormframework.bean;

import java.util.List;
import java.util.Map;

/**
 * 封装表结构信息
 *
 * @author xiaoming
 */
public class TableInfo {

    /**
     * 表名
     */
    private String tabName;

    /**
     * 所以字段信息
     */
    private Map<String, ColumnInfo> columns;

    /**
     * 主键
     */
    private ColumnInfo priKey;

    /**
     * 联合主键
     */
    private List<ColumnInfo> priKeys;


    public List<ColumnInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColumnInfo> priKeys) {
        this.priKeys = priKeys;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Map<String, ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnInfo> columns) {
        this.columns = columns;
    }

    public ColumnInfo getPriKey() {
        return priKey;
    }

    public void setPriKey(ColumnInfo priKey) {
        this.priKey = priKey;
    }

    public TableInfo(String tabName, Map<String, ColumnInfo> columns,
                     ColumnInfo priKey) {
        super();
        this.tabName = tabName;
        this.columns = columns;
        this.priKey = priKey;
    }

    public TableInfo(String tabName, Map<String, ColumnInfo> columns,
                     List<ColumnInfo> priKeys) {
        super();
        this.tabName = tabName;
        this.columns = columns;
        this.priKeys = priKeys;
    }

    public TableInfo() {
        super();
    }


}
