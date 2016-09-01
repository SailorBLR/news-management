package com.epam.hubarevich.utils;

import org.hibernate.Session;

import java.io.Serializable;


public class DeleteByIDUtil {
    public static boolean deleteById(Class<?> type, Serializable id, Session session) {
        Object persistentInstance = session.load(type, id);
        if (persistentInstance != null) {
            session.delete(persistentInstance);

            return true;
        }
        return false;
    }
}
