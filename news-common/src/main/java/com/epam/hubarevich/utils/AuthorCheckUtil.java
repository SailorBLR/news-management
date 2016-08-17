package com.epam.hubarevich.utils;

import com.epam.hubarevich.domain.Author;

import java.util.Calendar;

/**
 * Util class for checking inputed data
 */
public class AuthorCheckUtil {
    /**
     * Checks Author Name and expiration date
     * @param author Author object
     * @return true in case of correct data
     */
    public static boolean checkAuthorData (Author author) {
       if(author.getAuthorName()==null){
           return false;
       }
        if(author.getExpired()!=null&&author.getExpired().getTime()> Calendar.getInstance().getTimeInMillis()){
            return false;
        }
        return true;
    }
}
