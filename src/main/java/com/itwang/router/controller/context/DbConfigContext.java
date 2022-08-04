package com.itwang.router.controller.context;

public class DbConfigContext {
    public static ThreadLocal<String> dbIdxLocals = new ThreadLocal<>();

    public static ThreadLocal<String> tbIdxLocals = new ThreadLocal<>();

    public static void setDbIdx(Integer dbIdx){
        dbIdxLocals.set(String.valueOf(dbIdx));
    }

    public static void setTbIdx(Integer tbIdx){
        tbIdxLocals.set(String.valueOf(tbIdx));
    }

    public static void setDbIdx(String dbIdx){
        dbIdxLocals.set(dbIdx);
    }

    public static void setTbIdx(String tbIdx){
        tbIdxLocals.set(tbIdx);
    }

    public static String getDbIdx(){
        return dbIdxLocals.get();
    }
    public static String getTbIdx(){
        return tbIdxLocals.get();
    }



}
