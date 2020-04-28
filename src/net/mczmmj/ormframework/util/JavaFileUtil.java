package net.mczmmj.ormframework.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.mczmmj.ormframework.bean.ColumnInfo;
import net.mczmmj.ormframework.bean.JavaFieldGetSet;
import net.mczmmj.ormframework.bean.TableInfo;
import net.mczmmj.ormframework.core.DBManager;
import net.mczmmj.ormframework.core.TypeConvertor;

/**
 * 封装生成java源文件的常用操作
 * @author xiaoming
 *
 */
public class JavaFileUtil {

    /**
     *根据字段信息生成java属性信息
     * @param colum 字段信息
     * @param convertor 类型转换器
     * @return java属性的set/get方法
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo colum, TypeConvertor convertor){
        JavaFieldGetSet fieldSet = new JavaFieldGetSet();

        String javaFieldType= convertor.databaseType2JavaType(colum.getDataType());

        fieldSet.setFieldInfo("\tprivate "+javaFieldType+" "+colum.getColName()+";\n");

        StringBuffer getSrc=new StringBuffer();
        //public String getUser(){
        //生成get方法源码
        getSrc.append("\tpublic "+javaFieldType+" get"+StringUtil.firstChar2UpperCase(colum.getColName())+"(){\n"   );
        getSrc.append("\t\treturn "+colum.getColName()+";\n");
        getSrc.append("\t}\n");
        fieldSet.setGetInfo(getSrc.toString());

        //生成set方法源码
        StringBuffer setSrc= new StringBuffer();
        setSrc.append("\tpublic void set"+StringUtil.firstChar2UpperCase(colum.getColName())+"("   );
        setSrc.append(javaFieldType+" "+colum.getColName()+"){\n");
        setSrc.append("\t\tthis."+colum.getColName()+"="+colum.getColName()+";\n");
        setSrc.append("\t}\n");
        fieldSet.setSetInfo(setSrc.toString());
        return fieldSet;
    }


    /**
     * 根据表信息生成java类源码
     * @param tableInfo 表信息
     * @param convertor 数据类型转换器
     * @return java类源码
     */
    public  static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor){
        StringBuffer src= new StringBuffer();

        Map<String,ColumnInfo> columns=tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields=new ArrayList<JavaFieldGetSet>();
        for(ColumnInfo c:columns.values()){
            javaFields.add(createFieldGetSetSRC(c,convertor));
        }
        //生成package语句
        src.append("package "+ DBManager.getConf().getPoPackage()+";\n\n");

        //生成import语句
        src.append("import java.sql.*;\n");
        src.append("import java.util.*;\n\n");
        //生成类声明语句
        src.append("public class "+StringUtil.firstChar2UpperCase(tableInfo.getTabName())+" {\n\n");
//        System.out.println("public class "+StringUtil.firstChar2UpperCase(tableInfo.getTabName())+" {\n\n");


        //生成属性列表
        for(JavaFieldGetSet f:javaFields){
            src.append(f.getFieldInfo());
        }
        src.append("\n\n");

        //生成get方法列表
        for(JavaFieldGetSet f:javaFields){
            src.append(f.getGetInfo());
        }

        //生成set方法列表
        for(JavaFieldGetSet f:javaFields){
            src.append(f.getSetInfo());
        }

        //生成类结束
        src.append("}\n");
//        System.out.println(src);
        return src.toString();

    }
    /**
     *根据表信息生成java文件
     * @param table　表信息
     * @param convertor　类型转换器
     */
    public static void createJavaPOFile(TableInfo table,TypeConvertor convertor){
        String src=createJavaSrc(table, convertor);

        String srcPath=DBManager.getConf().getSrcPath()+"//";
        String packagePath=DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
        File f=new File(srcPath+packagePath);

        System.out.println(f.getAbsolutePath());

        if(!f.exists()){
            f.mkdirs();
        }

        BufferedWriter bw=null;
        try {
            bw=new BufferedWriter(new FileWriter(f.getAbsoluteFile()+"/"+StringUtil.firstChar2UpperCase(table.getTabName())+".java") );
            bw.write(src);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
