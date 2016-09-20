package com.epam.hubarevich.dao.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.Serializable;


public class DeleteByIDUtil {
    public static boolean deleteById(Class<?> type, Serializable id, Session session) throws HibernateException{
            Object persistentInstance = session.load(type, id);
            if (persistentInstance != null) {
                session.delete(persistentInstance);
                return true;
            }
        return false;
    }
}
