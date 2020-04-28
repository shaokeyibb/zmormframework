package net.mczmmj.ormframework.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mczmmj.ormframework.bean.ColumnInfo;
import net.mczmmj.ormframework.bean.TableInfo;
import net.mczmmj.ormframework.util.JDBCUtil;
import net.mczmmj.ormframework.util.ReflectUtil;

/**
 *    负责查询 对外提供服务的核心类
 * @author xiaoming
 */
public abstract class Query implements Cloneable{

    /**
     * 采用模板方法模式将JDBC操作封装成模板
     * @param sql sql语句
     * @param params sql参数　
     * @param clazz 记录要封装到的java类
     * @param back CallBack的实现类　回调
     * @return
     */
    public Object executeQueryTemplate(String sql,Object[] params, Class clazz,CallBack back){
        Connection conn=DBManager.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;

        try {
            ps=conn.prepareStatement(sql);
            JDBCUtil.handleParams(ps, params);
            rs=ps.executeQuery();
            return back.doExecute(conn, ps, rs);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }finally{
            DBManager.close(null, ps, conn);
        }
    }

    /**
     *    执行一条DML语句
     * @param sql 要执行的sql语句
     * @param params sql语句参数
     * @return 影响的记录行数
     */
    public int executeDML(String sql, Object[] params) {
        // TODO Auto-generated method stub
        Connection conn=DBManager.getConnection();
        int count=0;

        PreparedStatement ps=null;

        try {
            ps=conn.prepareStatement(sql);
            JDBCUtil.handleParams(ps, params);
            count=ps.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            DBManager.close(null, ps, conn);
        }
        return count;
    }

    /**
     *      将一个对象储存到数据库中
     * @param obj 要储存的对象
     */
    public void save(Object obj) {
        // TODO Auto-generated method stub
        Class c=obj.getClass();
        List<Object> params =new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        StringBuilder sql=new StringBuilder("insert into "+tableInfo.getTabName()+" (");
        Field[] fs=c.getDeclaredFields();
        int fieldNotNullCount=0;

        for(Field field:fs){
            String fieldName=field.getName();
            Object fieldValue = ReflectUtil.invokeGet(fieldName, obj);

            if(fieldValue!=null){
                fieldNotNullCount++;
                sql.append(fieldName+",");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1, ')');
        sql.append(" values (");
        for(int i=0;i<fieldNotNullCount;i++){
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1, ')');

        this.executeDML(sql.toString(), params.toArray());
    }

    /**
     *    删除数据库中的记录
     * @param clazz 对象类型
     * @param id 主键id
     * @return 影响的记录行数
     */
    @SuppressWarnings("rawtypes")
    public int delete(Class clazz, Object id) {
        // TODO Auto-generated method stub
        //通过class找TableInfo
        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
        //主键
        ColumnInfo onlyPriKey=tableInfo.getPriKey();

        String sql="delete from "+tableInfo.getTabName()+" where  "+onlyPriKey.getColName()+"=?";

        return executeDML(sql, new Object[]{id});

    }

    /**
     *    删除对应数据库中的记录
     * @param obj 要删除的对象
     * @return 影响的记录行数
     */

    public int delete(Object obj) {
        // TODO Auto-generated method stub
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPrikey  = tableInfo.getPriKey();

        //反射
        Object priKeyValue=ReflectUtil.invokeGet(onlyPrikey.getColName(), obj);
        return delete(c, priKeyValue);


    }

    /**
     *     更新对象对应的记录
     * @param obj 对象
     * @return 影响的记录行数
     */
    public int update(Object obj ,String[] fieldNames) {
        // TODO Auto-generated method stub
        Class c=obj.getClass();
        List<Object> params =new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo priKey = tableInfo.getPriKey();
        StringBuilder sql=new StringBuilder("update  "+tableInfo.getTabName()+" set ");

        for(String fieldName: fieldNames){
            Object fvalue = ReflectUtil.invokeGet(fieldName, obj);
            params.add(fvalue);
            sql.append(fieldName+"=?,");
        }
        params.add(ReflectUtil.invokeGet( priKey.getColName(),obj));

        sql.setCharAt(sql.length()-1,' ');
        sql.append(" where ");
        sql.append(priKey.getColName()+"=? ");
        System.out.println(sql.toString());
        return this.executeDML(sql.toString(), params.toArray());
    }

    /**
     *    查询数据库返回多条记录
     * @param sql  sql语句
     * @param clazz 封装javabean类的Class对象
     * @param params sql语句参数
     * @return 查询得到的结果集
     */
    @SuppressWarnings("rawtypes")
    public List queryRows(String sql, final Class clazz, Object[] params) {
        // TODO Auto-generated method stub
        return (List) executeQueryTemplate(sql, params, clazz, new CallBack() {

            @Override
            public Object doExecute(Connection conn, PreparedStatement ps,
                                    ResultSet rs) {
                ResultSetMetaData metaDate;
                List list=null;
                try {
                    metaDate = rs.getMetaData();
                    while(rs.next()){
                        if(list==null)
                            list=new ArrayList<>();
                        Object rowObj=clazz.newInstance();
                        for(int i=0;i<metaDate.getColumnCount();i++){
                            String columnName = metaDate.getColumnLabel(i+1);
                            Object columnValue = rs.getObject(i+1);
                            //通过反射调用rowObj的set方法
                            ReflectUtil.invokeSet(rowObj, columnName, columnValue);

                        }
                        list.add(rowObj);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return list;
            }
        });


    }


    /**
     *    查询数据库返回一条记录
     * @param sql  sql语句
     * @param clazz 封装javabean类的Class对象
     * @param params sql语句参数
     * @return 查询得到的结果对象
     */
    @SuppressWarnings("rawtypes")
    public Object queryUniqueRows(String sql, Class clazz,
                                  Object[] params) {
        // TODO Auto-generated method stub
        List list=queryRows(sql, clazz, params);
        return (list==null||list.size()==0)?null:list.get(0);
    }


    /**
     *    查询数据库返回一个值( 一行一列)
     * @param sql  sql语句
     * @param params sql语句参数
     * @return 查询得到的结果对象
     */
    public Object queryValue(String sql, Object[] params) {
        // TODO Auto-generated method stub

        return executeQueryTemplate(sql, params, null, new CallBack() {

            @Override
            public Object doExecute(Connection conn, PreparedStatement ps,
                                    ResultSet rs) {
                Object value = null;
                // TODO Auto-generated method stub
                try {
                    while(rs.next()){
                        value=rs.getObject(1);
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return value ;
            }
        });
    }

    /**
     *    查询数据库返回一个数字
     * @param sql  sql语句
     * @param params sql语句参数
     * @return 查询得到的结果数字
     */
    public Number queryNumber(String sql, Object[] params) {
        // TODO Auto-generated method stub
        return (Number) queryValue(sql, params);
    }

    /**
     * 分页查询　
     * @param pageNum 当前页
     * @param pageSize 每页显示的记录条数
     * @return
     */
    public abstract Object queryPagenate(int pageNum,int pageSize);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }


}