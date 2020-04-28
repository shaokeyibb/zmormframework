package net.mczmmj.ormframework.core;

import net.mczmmj.ormframework.bean.Configuration;
import net.mczmmj.ormframework.bean.connPool.DBconnPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 *  根据配置信息，维持连接对象的管理　
 * @author xiaoming
 *
 */
public class DBManager {

    /**
     * 配置信息
     */
    private static Configuration conf;
    /**
     * 连接池对象
     */
    private static DBconnPool pool ;
    public static Configuration getConf() {
        return conf;
    }
    static{
        Properties pros=new Properties();

        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        conf = new Configuration();
        conf.setDriver(pros.getProperty("driver"));
        conf.setPoPackage(pros.getProperty("poPackage"));
        conf.setPassword(pros.getProperty("password"));
        conf.setUrl(pros.getProperty("url"));
        conf.setUser(pros.getProperty("user"));
        conf.setSrcPath(pros.getProperty("srcPath"));
        conf.setUsingDB(pros.getProperty("usingDB"));
        conf.setQueryClass(pros.getProperty("queryClass"));
        conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));



    }
    /**
     * 获得数据库连接
     * @return 连接
     */
    public static Connection createConnection(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPassword());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 从连接池中获得数据库连接
     * @return 连接
     */
    public static Connection getConnection(){
//        try {
//            Class.forName(conf.getDriver());
//            return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPassword());
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
        if(pool==null)
            pool = new DBconnPool();
        return pool.getConnection();

    }
    /**
     * 关闭资源
     * @param rs
     * @param ps
     * @param conn
     */
    public static void close(ResultSet rs,Statement ps,Connection conn){
        try {
            if(rs!=null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pool.close(conn);
    }
}