package com.aura.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_time {
    public static void main(String[] args) {
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");;
        long li = new Long("1546675551000");
        Date date = new Date(li);
        String format = datetime.format(date);
        System.out.println(format);

    }
}
