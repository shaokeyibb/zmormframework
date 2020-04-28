package net.mczmmj.ormframework.bean;

/**
 * 管理配置信息
 *
 * @author xiaoming
 */
public class Configuration {
    /**
     * 驱动类
     */
    private String driver;
    /**
     * jdbc-url
     */
    private String url;
    /**
     * 数据库用户名
     */
    private String user;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 使用的数据库
     */
    private String usingDB;
    /**
     * 项目的源码路径
     */
    private String srcPath;
    /**
     * 扫描生成java类的包
     * persistence object
     */
    private String poPackage;

    /**
     * 连接池中最小的连接数
     */
    private int poolMinSize;
    /**
     * 连接池中最大的连接数
     */
    private int poolMaxSize;


    /**
     * 项目使用的查询类
     */
    private String queryClass;

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }

    public Configuration(String driver, String url, String user,
                         String password, String usingDB,
                         String srcPath, String poPackage) {
        super();
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.usingDB = usingDB;
        this.srcPath = srcPath;
        this.poPackage = poPackage;
    }

    public int getPoolMinSize() {
        return poolMinSize;
    }

    public void setPoolMinSize(int poolMinSize) {
        this.poolMinSize = poolMinSize;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }

    public Configuration() {
        super();
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }


}