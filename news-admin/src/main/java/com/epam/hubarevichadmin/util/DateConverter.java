package com.epam.hubarevichadmin.util;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anton_Hubarevich on 8/5/2016.
 */
public class DateConverter implements Converter<String,Date> {
    @Override
    public Date convert(String s) {
        if (s != null&& !s.equals("")) {
            s=s.replace("T"," ");
            Date date=null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }

       return null;
    }
}
