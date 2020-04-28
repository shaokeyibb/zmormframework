package net.mczmmj.ormframework.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.mczmmj.ormframework.bean.ColumnInfo;
import net.mczmmj.ormframework.bean.TableInfo;
import net.mczmmj.ormframework.core.imp.MysqlTypeConvertor;
import net.mczmmj.ormframework.util.JavaFileUtil;
import net.mczmmj.ormframework.util.StringUtil;


/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构。
 *
 */
public class TableContext {

    /**
     * 表名为key，表信息对象为value
     */
    public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

    private TableContext() {
    }

    static {
        try {
            //初始化获得表的信息
            Connection con = DBManager.getConnection();
            DatabaseMetaData dbmd = con.getMetaData();

            ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});

            while (tableRet.next()) {
                String tableName = (String) tableRet.getObject("TABLE_NAME");

                TableInfo ti = new TableInfo(tableName, new HashMap<String, ColumnInfo>(), new ArrayList<ColumnInfo>());
                tables.put(tableName, ti);

                ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  //查询表中的所有字段
                while (set.next()) {
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"), 0);
                    ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
                }

                ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  //查询t_user表中的主键
                while (set2.next()) {
                    ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
                    ci2.setKeyType(ColumnInfo.PRIMARY_KEY);  //设置为主键类型
                    ti.getPriKeys().add(ci2);
                }

                if (ti.getPriKeys().size() > 0) {  //取唯一主键。。方便使用。如果是联合主键。则为空！
                    ti.setPriKey(ti.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //更新类结构
        updateJavaPOFile();

        //加载po包下的所有的类
        loadPOTables();


    }

    /**
     * 根据表结构，更新配置po包下的java类
     */
    public static void updateJavaPOFile() {
        Map<String, TableInfo> map = tables;
        for (TableInfo t : map.values()) {
            JavaFileUtil.createJavaPOFile(t, new MysqlTypeConvertor());
        }
    }

    /**
     * 加载po包下的类
     */
    public static void loadPOTables() {
        for (TableInfo tableInfo : tables.values()) {
            try {
                Class c = Class.forName(DBManager.getConf().getPoPackage() +
                        "." + StringUtil.firstChar2UpperCase(tableInfo.getTabName()));
                poClassTableMap.put(c, tableInfo);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


//    public static void main(String[] args) {
//         Map<String,TableInfo>  tables = TableContext.tables;
//         for(String v: tables.keySet())
//         {
//            System.out.println(v);
//         }
//         for(TableInfo f:tables.values()){
//            System.out.println(f.getPriKey().getColName());
//         }
//         System.out.println(tables);
//    }

}
