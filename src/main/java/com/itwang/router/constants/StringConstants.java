package com.itwang.router.constants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringConstants {
    public static final String CONFIG_PREFIX = "dbrouter.config";
    public static final DateTimeFormatter EXPORT_TIME_FORMATTER =DateTimeFormatter.ofPattern("YYYY/MM/dd");
    public static final Pattern pattern = Pattern.compile("(\\-\\w\\d+)\\w\\s");

    public static final Pattern NUMBER = Pattern.compile("(\\D+\\d+)");

    public static final Pattern NUMBER_START = Pattern.compile("^([\\s]*\\d+)");


    public static void main(String[] args) {
       String a =" 12fasfdsl";
       String b = "f14522";
        System.out.println(NUMBER_START.matcher(a).find());
        System.out.println(NUMBER_START.matcher(b).find());
        String substring = b.substring(0, 2);
        System.out.println(substring);
        int i = a.indexOf("*");

        System.out.println(i);
        BigDecimal one = new BigDecimal(1);
        int i1 = one.compareTo(null);


    }
}
