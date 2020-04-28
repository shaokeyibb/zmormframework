package net.mczmmj.ormframework.core;

/**
 * 创建query工厂类
 * @author xiaoming
 *
 */
public class QueryFactory {

    private static QueryFactory factory=new QueryFactory();
    private static Query prototypeObj;
    static{
        try {
            Class c=Class.forName(DBManager.getConf().getQueryClass());
            prototypeObj=(Query) c.newInstance();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private QueryFactory(){
    }

    public static QueryFactory getInstance(){
        return factory;
    }


    public Query creatyFactory(){
        try {
            return (Query) prototypeObj.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
