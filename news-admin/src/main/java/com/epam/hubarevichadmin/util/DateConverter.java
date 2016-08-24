package com.epam.hubarevichadmin.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConverter implements Converter<String,Date> {
	private static final Logger LOG = LogManager.getLogger(DateConverter.class);
	
	
    @Override
    public Date convert(String s) {
        if (s != null&& !"".equals(s)) {
            s=s.replace("T"," ");
            Date date=null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = simpleDateFormat.parse(s);
            } catch (ParseException e) {
                LOG.error(e);
            }
            return date;
        }
       return null;
    }
}
