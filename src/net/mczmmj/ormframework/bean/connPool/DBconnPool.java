package net.mczmmj.ormframework.bean.connPool;

import net.mczmmj.ormframework.core.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池
 *
 * @author frank
 */
public class DBconnPool {
    /**
     * 连接池对象
     */
    private List<Connection> pool;
    /**
     * 最大连接数
     */
    private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
    /**
     * 最小连接数
     */
    private static final int POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();


    /**
     * 初始化连接池
     */
    public void initPool() {
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }
        while (pool.size() < POOL_MIN_SIZE) {
            pool.add(DBManager.createConnection());
        }
    }

    /**
     * 从连接池中取出一个连接
     *
     * @return 连接
     */
    public synchronized Connection getConnection() {
        int last_index = pool.size() - 1;
        Connection conn = pool.get(last_index);
        pool.remove(last_index);
        return conn;
    }

    /**
     * 将连接放回池中
     *
     * @param conn 连接
     */
    public synchronized void close(Connection conn) {
        if (pool.size() >= POOL_MAX_SIZE)
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        else
            pool.add(conn);

    }


    public DBconnPool() {
        initPool();
    }

}
