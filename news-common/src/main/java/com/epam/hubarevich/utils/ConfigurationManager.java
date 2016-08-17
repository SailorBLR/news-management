package com.epam.hubarevich.utils;

import java.util.ResourceBundle;

/**
 * Util class
 * Configures resource source
 */
public class ConfigurationManager {

    private final static String RES = "config";
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(RES);
    private ConfigurationManager() { }

    /**
     * Gets the property by key
     * @param key String property name
     * @return  String property value
     */
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
