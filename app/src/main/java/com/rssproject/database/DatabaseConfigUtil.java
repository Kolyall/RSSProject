package com.rssproject.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.rssproject.Enclosure;
import com.rssproject.Item;

/**
 * Created by Unuchek on 02.10.2015.
 */


public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            Item.class, Enclosure.class
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }

}
