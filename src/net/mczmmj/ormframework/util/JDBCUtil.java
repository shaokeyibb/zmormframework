package net.mczmmj.ormframework.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * 封装jdbc常用的操作
 * @author xiaoming
 *
 */
public class JDBCUtil {

    /**
     *给preparedStatement传如参数　
     * @param ps PreparedStatement
     * @param params 参数
     */
    public static void handleParams( PreparedStatement ps,Object[] params){

        if(params!=null){
            for(int i=0;i<params.length;i++){
                try {
                    ps.setObject(i+1,params[i]);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
