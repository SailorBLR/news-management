package com.epam.hubarevichadmin.util;

import com.epam.hubarevich.domain.Tag;
import org.springframework.core.convert.converter.Converter;


public class StringToTagConverter implements Converter<String,Tag> {
    @Override
    public Tag convert(String string) {
        System.out.println(string);
        Tag tag = new Tag();
        tag.setTagId(Long.valueOf(string));
        return tag;
    }
}
