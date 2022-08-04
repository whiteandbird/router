package com.itwang.router.utils;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class PropertyUtil {
    private static int version = 1;


    static {
        try {
            Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
        }catch (Exception e){
            version = 2;
        }

    }

    /**
     *
     * @param environment 环境
     * @param prefix      解析前缀
     * @param targetClass  目标类
     * @param <T>
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    public static <T> T handle(final Environment environment, final String prefix, final Class<T> targetClass){
        String standPrefix = prefix.endsWith(".") ? prefix : prefix+".";
        if(version == 1){
            return (T) v1(environment, standPrefix);
        }
        return (T) v2(environment, standPrefix, targetClass);
    }


    private static Object v1(final Environment environment, final String prefix){
        try{
            Class<?> clzz = Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
            Constructor<?> declaredConstructor = clzz.getDeclaredConstructor(PropertyResolver.class);
            Object o = declaredConstructor.newInstance(environment);
            Method getSubPropertiesMethod = clzz.getDeclaredMethod("getSubProperties");
            return getSubPropertiesMethod.invoke(o, prefix);
        }catch (ClassNotFoundException| InstantiationException |IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            e.printStackTrace();
            throw new RuntimeException("转化失败");
        }
    }

    private static Object v2(final Environment environment, final String prefix, final Class<?> targetClass){
        String noPointPrefix = null;
        try {
            Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
            Method getMethod = binderClass.getDeclaredMethod("get", Environment.class);
            Method bindMethod = binderClass.getDeclaredMethod("bind", String.class, Class.class);

            // 拿到binder实例
            // 处理后缀.不需要后缀.
            noPointPrefix =  prefix.endsWith(".") ? prefix.substring(0, prefix.length()-1) : prefix;
            Object binderInstance = getMethod.invoke(null, environment);
            Object bindResult =  bindMethod.invoke(binderInstance, noPointPrefix, targetClass);
            Method bindGetMethod = bindResult.getClass().getDeclaredMethod("get");
            return bindGetMethod.invoke(bindResult);


        }catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
            throw new RuntimeException("转化失败,{}"+ noPointPrefix);
        }

    }

    public static void main(String[] args) {
        boolean matches = Pattern.matches("wang", "wan");
        System.out.println(matches);
    }
}
