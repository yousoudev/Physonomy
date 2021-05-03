package me.joost.Physonomy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


    public static Date getDateNow() {
        return new Date();
    }

    public static String dateToString(Date now){
        return sdf.format(now);
    }

    public static Date stringToDate(String now){
        try{
            return sdf.parse(now);
        }catch(ParseException e){
            return null;
        }
    }
}
