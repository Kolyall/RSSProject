package com.rssproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rssproject.objects.Enclosure;
import com.rssproject.objects.Item;
import com.rssproject.R;

import java.sql.SQLException;

/**
 * Created by Unuchek on 02.10.2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "rss_database.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;
    // the DAO object we use to access the SimpleData table
    private Dao<Item, Integer> simpleDao = null;
    private RuntimeExceptionDao<Item, Integer> simpleRuntimeDao = null;
    private RuntimeExceptionDao<Enclosure, Integer> enclosureRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }
    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Item.class);
            TableUtils.createTable(connectionSource, Enclosure.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Item.class, true);
            TableUtils.dropTable(connectionSource, Enclosure.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Item, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(Item.class);
        }
        return simpleDao;
    }
    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Item, Integer> getItemDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(Item.class);
        }
        return simpleRuntimeDao;
    }
    public RuntimeExceptionDao<Enclosure, Integer> getEnclosureDataDao() {
        if (enclosureRuntimeDao == null) {
            enclosureRuntimeDao = getRuntimeExceptionDao(Enclosure.class);
        }
        return enclosureRuntimeDao;
    }
    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
        simpleDao = null;
        simpleRuntimeDao = null;
    }
}