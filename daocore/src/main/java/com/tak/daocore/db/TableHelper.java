package com.tak.daocore.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.harmonywisdom.framework.library.annotation.Column;
import com.harmonywisdom.framework.library.annotation.Id;
import com.harmonywisdom.framework.library.annotation.Table;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:14
 */
public class TableHelper {

    private static final String TAG = "TableHelper";

    public static <T> void createTablesByClasses(SQLiteDatabase db,
                                                 Class<?>[] clazzs) {
        for (Class<?> clazz : clazzs)
            createTable(db, clazz);
    }

    public static <T> void dropTablesByClasses(SQLiteDatabase db,
                                               Class<?>[] clazzs) {
        for (Class<?> clazz : clazzs)
            dropTable(db, clazz);
    }

    public static <T> void createTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(tableName).append(" (");

        List<Field> allFields = TableHelper
                .joinFields(clazz.getDeclaredFields(), clazz.getSuperclass()
                        .getDeclaredFields());
        for (Field field : allFields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            Column column = (Column) field.getAnnotation(Column.class);

            String columnType = "";
            if (column.type().equals(""))
                columnType = getColumnType(field.getType());
            else {
                columnType = column.type();
            }

            sb.append(column.name() + " " + columnType);

            if (column.length() != 0) {
                sb.append("(" + column.length() + ")");
            }

            if ((field.isAnnotationPresent(Id.class)) // update 2012-06-10 实体类定义为Integer类型后不能生成Id异常
                    && ((field.getType() == Integer.TYPE) || (field.getType() == Integer.class)))
                sb.append(" primary key autoincrement");
            else if (field.isAnnotationPresent(Id.class)) {
                sb.append(" primary key");
            }

            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(")");

        String sql = sb.toString();

        Log.d(TAG, "crate table [" + tableName + "]: " + sql);

        db.execSQL(sql);
    }

    public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "DROP TABLE IF EXISTS " + tableName;
        Log.d(TAG, "dropTable[" + tableName + "]:" + sql);
        db.execSQL(sql);
    }

    private static String getColumnType(Class<?> fieldType) {
        if (String.class == fieldType) {
            return "TEXT";
        }
        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
            return "INTEGER";
        }
        if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
            return "BIGINT";
        }
        if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
            return "FLOAT";
        }
        if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
            return "INT";
        }
        if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
            return "DOUBLE";
        }
        if (Blob.class == fieldType) {
            return "BLOB";
        }

        return "TEXT";
    }

    // 合并Field数组并去重,并实现过滤掉非Column字段,和实现Id放在首字段位置功能
    public static List<Field> joinFields(Field[] fields1, Field[] fields2) {
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        for (Field field : fields1) {
            // 过滤掉非Column定义的字段
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = (Column) field.getAnnotation(Column.class);
            map.put(column.name(), field);
        }
        for (Field field : fields2) {
            // 过滤掉非Column定义的字段
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = (Column) field.getAnnotation(Column.class);
            if (!map.containsKey(column.name())) {
                map.put(column.name(), field);
            }
        }
        List<Field> list = new ArrayList<Field>();
        for (String key : map.keySet()) {
            Field tempField = map.get(key);
            // 如果是Id则放在首位置.
            if (tempField.isAnnotationPresent(Id.class)) {
                list.add(0, tempField);
            } else {
                list.add(tempField);
            }
        }
        return list;
    }
}
