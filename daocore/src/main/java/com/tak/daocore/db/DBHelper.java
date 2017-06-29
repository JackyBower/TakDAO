package com.tak.daocore.db;

import android.content.Context;

import com.tak.daocore.dao.impl.BaseDaoImpl;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 16:34
 */
@SuppressWarnings("all")
public class DBHelper {

    private static final DBHelper ourInstance = new DBHelper();
    //获取SQLiteDBHelper对象
    private SQLiteDBHelper mSQLiteDBHelper;
    //默认数据库名
    private String databaseName = "_database.db";
    // 数据库版本号
    private int dbVersion = 1;

    public static DBHelper getInstance() {
        return ourInstance;
    }

    private DBHelper() {
    }

    /**
     * 获取SQLiteDBHelper对象
     * @param context
     * @return
     */
    public SQLiteDBHelper getSQLiteDBHelper(Context context) {
        if (mSQLiteDBHelper == null) {
            mSQLiteDBHelper = new SQLiteDBHelper(context, getDatabaseName(), getDbVersion(), null);
        }
        return mSQLiteDBHelper;
    }

    /**
     * 获取BaseDaoImpl对象
     * @param context
     * @param clz
     * @return
     */
    public BaseDaoImpl getBaseDao(Context context, Class<?> clz) {
        //获取BaseDaoImpl对象
        BaseDaoImpl baseDao = new BaseDaoImpl(mSQLiteDBHelper, clz);
        //判断是否创建了表
        if (!baseDao.isTableExist()) {
            mSQLiteDBHelper.onCreate(mSQLiteDBHelper.getWritableDatabase());
        }
        return baseDao;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public SQLiteDBHelper getSQLiteDBHelper() {
        return mSQLiteDBHelper;
    }

    public void setSQLiteDBHelper(SQLiteDBHelper mSQLiteDBHelper) {
        this.mSQLiteDBHelper = mSQLiteDBHelper;
    }
}
