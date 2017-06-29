package com.tak.daocore.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.tak.daocore.annotation.Column;
import com.tak.daocore.annotation.Id;
import com.tak.daocore.annotation.Table;
import com.tak.daocore.dao.IBaseDao;
import com.tak.daocore.dao.exception.DAOException;
import com.tak.daocore.dao.exception.QueryException;
import com.tak.daocore.dao.impl.query.QueryCondition;
import com.tak.daocore.dao.impl.result.QueryResult;
import com.tak.daocore.db.TableHelper;
import com.tak.daocore.model.Paging;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 16:24
 */
@SuppressWarnings("all")
public class BaseDaoImpl<T> implements IBaseDao<T> {

    private String TAG = "BaseDaoImpl";
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private String tableName;
    private String idColumn;
    private Class<T> clazz;
    private List<Field> allFields;
    private static final int METHOD_INSERT = 0;
    private static final int METHOD_UPDATE = 1;

    private static final int TYPE_NOT_INCREMENT = 0;
    private static final int TYPE_INCREMENT = 1;

    public BaseDaoImpl(SQLiteOpenHelper dbHelper, Class<T> clazz) {
        this.dbHelper = dbHelper;
        if (clazz == null) {
            this.clazz = ((Class<T>) ((java.lang.reflect.ParameterizedType) super
                    .getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0]);
        } else {
            this.clazz = clazz;
        }

        if (this.clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) this.clazz.getAnnotation(Table.class);
            this.tableName = table.name();
        }

        // 加载所有字段
        this.allFields = TableHelper.joinFields(this.clazz.getDeclaredFields(),
                this.clazz.getSuperclass().getDeclaredFields());

        // 找到主键
        for (Field field : this.allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                Column column = (Column) field.getAnnotation(Column.class);
                this.idColumn = column.name();
                break;
            }
        }

        Log.d(TAG, "clazz:" + this.clazz + " tableName:" + this.tableName
                + " idColumn:" + this.idColumn);
    }

    public BaseDaoImpl(SQLiteOpenHelper dbHelper) {
        this(dbHelper, null);
    }

    public SQLiteOpenHelper getDbHelper() {
        return dbHelper;
    }

    public T get(int id) {
        return get(String.valueOf(id));
    }

    @Override
    public T get(String id) {
        String selection = this.idColumn + " = ?";
        String[] selectionArgs = {id};
        Log.d(TAG, "[get]: select * from " + this.tableName + " where "
                + this.idColumn + " = '" + id + "'");
        List<T> list = find(null, selection, selectionArgs, null, null, null,
                null);
        if ((list != null) && (list.size() > 0)) {
            return (T) list.get(0);
        }
        return null;
    }

    @Override
    public List<T> rawQuery(String sql, String[] selectionArgs) {
        Log.d(TAG, "[rawQuery]: " + getLogSql(sql, selectionArgs));

        List<T> list = new ArrayList<T>();
        Cursor cursor = null;
        try {
            if (database == null) {
                database = this.dbHelper.getReadableDatabase();
            }
            cursor = database.rawQuery(sql, selectionArgs);
            getListFromCursor(list, cursor);
        } catch (Exception e) {
            Log.e(this.TAG, "[rawQuery] from DB Exception.");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public boolean isTableExist() {
        boolean isExist = false;
        if (TextUtils.isEmpty(tableName))
            return isExist;
        String sql = "select name from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
        return isExist(sql, null);
    }

    @Override
    public boolean isExist(String sql, String[] selectionArgs) {
        Log.d(TAG, "[isExist]: " + getLogSql(sql, selectionArgs));
        Cursor cursor = null;
        try {
            if (database == null) {
                database = this.dbHelper.getReadableDatabase();
            }
            cursor = database.rawQuery(sql, selectionArgs);
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(this.TAG, "[isExist] from DB Exception." + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    @Override
    public List<T> findAll() {
        return find(null, null, null, null, null, null, null);
    }

    @Override
    public List<T> find(String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        List<T> list = new ArrayList<T>();
        Cursor cursor = null;
        try {
            if (database == null) {
                database = this.dbHelper.getReadableDatabase();
            }
            cursor = database.query(this.tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            getListFromCursor(list, cursor);
        } catch (Exception e) {
            Log.e(this.TAG, "[find] from DB Exception" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    private void getListFromCursor(List<T> list, Cursor cursor)
            throws IllegalAccessException, InstantiationException {
        while (cursor.moveToNext()) {
            T entity = this.clazz.newInstance();

            for (Field field : this.allFields) {
                Column column = null;
                if (field.isAnnotationPresent(Column.class)) {
                    column = (Column) field.getAnnotation(Column.class);

                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();

                    int c = cursor.getColumnIndex(column.name());
                    if (c < 0) {
                        continue; // 如果不存则循环下个属性值
                    } else if ((Integer.TYPE == fieldType)
                            || (Integer.class == fieldType)) {
                        field.set(entity, cursor.getInt(c));
                    } else if (String.class == fieldType) {
                        field.set(entity, cursor.getString(c));
                    } else if ((Long.TYPE == fieldType)
                            || (Long.class == fieldType)) {
                        field.set(entity, Long.valueOf(cursor.getLong(c)));
                    } else if ((Float.TYPE == fieldType)
                            || (Float.class == fieldType)) {
                        field.set(entity, Float.valueOf(cursor.getFloat(c)));
                    } else if ((Short.TYPE == fieldType)
                            || (Short.class == fieldType)) {
                        field.set(entity, Short.valueOf(cursor.getShort(c)));
                    } else if ((Double.TYPE == fieldType)
                            || (Double.class == fieldType)) {
                        field.set(entity, Double.valueOf(cursor.getDouble(c)));
                    } else if (Date.class == fieldType) {// 处理java.util.Date类型,update2012-06-10
                        Date date = new Date();
                        date.setTime(cursor.getLong(c));
                        field.set(entity, date);
                    } else if (Blob.class == fieldType) {
                        field.set(entity, cursor.getBlob(c));
                    } else if (Character.TYPE == fieldType) {
                        String fieldValue = cursor.getString(c);

                        if ((fieldValue != null) && (fieldValue.length() > 0)) {
                            field.set(entity, Character.valueOf(fieldValue
                                    .charAt(0)));
                        }
                    }
                }
            }
            list.add((T) entity);
        }
    }

    @Override
    public long save(T entity) {
        return save(entity, true);
    }

    @Override
    public long save(T entity, boolean flag) {
        String sql = "";
        try {
            if (database == null) {
                database = this.dbHelper.getWritableDatabase();
            }
            ContentValues cv = new ContentValues();
            if (flag) {
                sql = setContentValues(entity, cv, TYPE_INCREMENT,
                        METHOD_INSERT);// id自增
            } else {
                sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
                        METHOD_INSERT);// id需指定
            }
            Log.d(TAG, "[insert]: insert into " + this.tableName + " " + sql);
            long row = database.insert(this.tableName, null, cv);
            return row;
        } catch (Exception e) {
            Log.d(this.TAG, "[insert] into DB Exception." + e.getMessage());
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void delete(String id) {
        if (database == null) {
            database = this.dbHelper.getWritableDatabase();
        }
        String where = this.idColumn + " = ?";
        String[] whereValue = {id};

        Log.d(TAG, "[delete]: delelte from " + this.tableName + " where "
                + where.replace("?", String.valueOf(id)));

        database.delete(this.tableName, where, whereValue);
    }

    @Override
    public void delete(String... ids) {
        if (ids.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            if (database == null) {
                database = this.dbHelper.getWritableDatabase();
            }
            String sql = "delete from " + this.tableName + " where "
                    + this.idColumn + " in (" + sb + ")";

            Log.d(TAG, "[delete]: " + getLogSql(sql, ids));

            database.execSQL(sql, (Object[]) ids);
        }
    }

    @Override
    public void deleteAll() {
        executeJPQL("delete from " + this.tableName, null);
        Log.d(TAG, "[delete]: delete from" + this.tableName);
    }

    @Override
    public void update(T entity) {
        try {
            if (database == null) {
                database = this.dbHelper.getWritableDatabase();
            }
            ContentValues cv = new ContentValues();

            String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
                    METHOD_UPDATE);

            String where = this.idColumn + " = ?";
//            int id = Integer.parseInt(cv.get(this.idColumn).toString());
            String id = cv.get(this.idColumn).toString();
            cv.remove(this.idColumn);

            Log.d(TAG, "[update]: update " + this.tableName + " set " + sql
                    + " where " + where.replace("?", String.valueOf(id)));

            String[] whereValue = {id};
            database.update(this.tableName, cv, where, whereValue);
        } catch (Exception e) {
            Log.d(this.TAG, "[update] DB Exception." + e.getMessage());
            e.printStackTrace();
        }
    }

    private String setContentValues(T entity, ContentValues cv, int type,
                                    int method) throws IllegalAccessException {
        StringBuffer strField = new StringBuffer("(");
        StringBuffer strValue = new StringBuffer(" values(");
        StringBuffer strUpdate = new StringBuffer(" ");
        for (Field field : this.allFields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = (Column) field.getAnnotation(Column.class);

            field.setAccessible(true);
            Object fieldValue = field.get(entity);
            if (fieldValue == null)
                continue;
            if ((type == TYPE_INCREMENT)
                    && (field.isAnnotationPresent(Id.class))) {
                continue;
            }
            if (Date.class == field.getType()) {// 处理java.util.Date类型,update
                // 2012-06-10
                cv.put(column.name(), ((Date) fieldValue).getTime());
                continue;
            }
            String value = String.valueOf(fieldValue);
            cv.put(column.name(), value);
            if (method == METHOD_INSERT) {
                strField.append(column.name()).append(",");
                strValue.append("'").append(value).append("',");
            } else {
                strUpdate.append(column.name()).append("=").append("'").append(
                        value).append("',");
            }

        }
        if (method == METHOD_INSERT) {
            strField.deleteCharAt(strField.length() - 1).append(")");
            strValue.deleteCharAt(strValue.length() - 1).append(")");
            return strField.toString() + strValue.toString();
        } else {
            return strUpdate.deleteCharAt(strUpdate.length() - 1).append(" ")
                    .toString();
        }
    }

    /**
     * 将查询的结果保存为名值对map.
     *
     * @param sql           查询sql
     * @param selectionArgs 参数值
     * @return 返回的Map中的key全部是小写形式.
     */
    @Override
    public List<Map<String, String>> query2MapList(String sql,
                                                   String[] selectionArgs) {
        Log.d(TAG, "[query2MapList]: " + getLogSql(sql, selectionArgs));
        Cursor cursor = null;
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        try {
            if (database == null) {
                database = this.dbHelper.getReadableDatabase();
            }
            cursor = database.rawQuery(sql, selectionArgs);
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                for (String columnName : cursor.getColumnNames()) {
                    int c = cursor.getColumnIndex(columnName);
                    if (c < 0) {
                        continue; // 如果不存在循环下个属性值
                    } else {
                        map.put(columnName.toLowerCase(), cursor.getString(c));
                    }
                }
                retList.add(map);
            }
        } catch (Exception e) {
            Log.e(TAG, "[query2MapList] from DB exception" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return retList;
    }

    /**
     * 封装执行sql代码.
     *
     * @param sql
     * @param selectionArgs
     */
    @Override
    public void executeJPQL(String sql, Object[] selectionArgs) {
        Log.d(TAG, "[execSql]: " + getLogSql(sql, selectionArgs));
        try {
            if (database == null) {
                database = this.dbHelper.getWritableDatabase();
            }
            if (selectionArgs == null) {
                database.execSQL(sql);
            } else {
                database.execSQL(sql, selectionArgs);
            }
        } catch (Exception e) {
            Log.e(TAG, "[execSql] DB exception." + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getLogSql(String sql, Object[] args) {
        if (args == null || args.length == 0) {
            return sql;
        }
        for (int i = 0; i < args.length; i++) {
            sql = sql.replaceFirst("\\?", "'" + String.valueOf(args[i]) + "'");
        }
        return sql;
    }

    @Override
    public QueryResult<T> find(QueryCondition condition) {
        QueryResult<T> result = null;
        try {
            String queryClause = condition.getQueryClause();
            String queryOrderBy = condition.getQueryOrderBy();
            result = this.find(queryClause, queryOrderBy, condition.getPaging(), condition.getParam() == null ? null : condition.getParam().getParamValues());
        } catch (QueryException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public QueryResult<T> find(String namedWhereClause, String nameOrderbyClause, Paging paging, Map<String, Object> params) throws DAOException {
        QueryResult result = new QueryResult();
        List<T> list = find(null, setParams(namedWhereClause, params), null, null, null, nameOrderbyClause, paging.getStartIndex() + "," + paging.getPageSize());
        if (paging != null) {
            result.setTotal(find(new String[]{"id"}, setParams(namedWhereClause, params), null, null, null, nameOrderbyClause, null).size());
        }
        result.setRows(list);
        if (paging == null) {
            result.setTotal((long) result.getRows().size());
        }
        return result;
    }

    @Override
    public void closeDB() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    private String setParams(String querySql, Map<String, Object> params) {
        if (params != null) {
            String[] fileds = new String[params.size()];
            Iterator i$ = params.entrySet().iterator();
            int s = 0;
            while (i$.hasNext()) {
                Map.Entry entry = (Map.Entry) i$.next();
                fileds[s] = ":" + entry.getKey();
                s++;
            }
            int i = 0;
            for (Iterator iter = params.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                //找到关键字IN，去掉两边的引号
                if (querySql.contains("IN :")) {
                    querySql = querySql.replace(fileds[i], "" + entry.getValue() + "");
                } else {
                    querySql = querySql.replace(fileds[i], "'" + entry.getValue() + "'");
                }
                i++;
            }
        }
        return querySql;
    }
}
