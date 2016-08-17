package com.epam.hubarevich.utils;

import java.sql.Timestamp;
import java.util.Calendar;


public class DateUtil {
    public static Timestamp makeTimeStampNow(){
        Calendar calendar = Calendar.getInstance();
        return new Timestamp(calendar.getTime().getTime());
    }
}
