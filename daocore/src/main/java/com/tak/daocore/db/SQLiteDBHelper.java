package com.tak.daocore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:15
 */
public class SQLiteDBHelper extends SQLiteOpenHelper {

    public Class<?>[] modelClasses;

    /**
     * SQLiteDBHelper操作对象
     *
     * @param context
     * @param databaseName    数据库名称
     * @param databaseVersion 数据库版本
     * @param modelClasses    实体对象
     */
    public SQLiteDBHelper(Context context, String databaseName, int databaseVersion,
                          Class<?>[] modelClasses) {
        super(context, databaseName, null, databaseVersion);
        this.modelClasses = modelClasses;
    }

    public SQLiteDBHelper(Context context, String databaseName,
                          SQLiteDatabase.CursorFactory factory, int databaseVersion,
                          Class<?>[] modelClasses) {
        super(context, databaseName, factory, databaseVersion);
        this.modelClasses = modelClasses;
    }

    /**
     * 如果没有数据库中没有此表 就创建表结构 覆写超类的创建的方法
     * 这个方法在超类中是一个只有方法体没有实现体的
     * onCreate 创建方法 在用户执行调用获取用户数据库管理时例就已经执行
     * 此方法是超类存在的 并且由 getWritableDatabase()这个方法调用的
     */
    public void onCreate(SQLiteDatabase db) {
        TableHelper.createTablesByClasses(db, this.modelClasses);
    }

    /**
     * 执行更新 如果表存在 将执行更新操作
     *
     * @param db
     * @param oldVersion 老版本号
     * @param newVersion 新版本号
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableHelper.dropTablesByClasses(db, this.modelClasses);
        onCreate(db);

        /*int upgradeVersion = oldVersion;
        if (1 == upgradeVersion) {
            db.execSQL("ALTER TABLE t_emergency_knowledge ADD content TEXT;");
            upgradeVersion = 2;
        }
        if (upgradeVersion != newVersion) {
            TableHelper.dropTablesByClasses(db, this.modelClasses);
            onCreate(db);
        }*/
    }
}
