package com.itwang.router;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Test {

    public String hello = "hello world";

    public Test(){
        System.out.println("c is init");
    }
    private static final Pattern IS_NUMBER = Pattern.compile("^\\d*[\\.\\d*]*");
    static {
        num = 3;
    }

    private static int num = 4;

    public static void main(String[] args) throws Exception{
        BigDecimal bigDecimal = new BigDecimal("12.000").stripTrailingZeros();
        System.out.println(bigDecimal);


    }

    public static void a(){
        String a = "fasdfasf";
        b(a);
        System.out.println(a);
    }
    public static void b(String c){
        c="fdsfs";
    }
}
