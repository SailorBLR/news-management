package com.epam.hubarevich.utils;


import com.epam.hubarevich.domain.Tag;

public class TagCheckUtil {
    public static boolean checkTag (Tag tag){

        if(tag.getTagName()==null
                || tag.getTagName().length()<2
                || tag.getTagName().length()>20) {
            return false;
        }
        return true;
    }
}
