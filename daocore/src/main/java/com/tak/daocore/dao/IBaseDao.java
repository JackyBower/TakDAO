package com.tak.daocore.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.tak.daocore.dao.exception.DAOException;
import com.tak.daocore.dao.impl.query.QueryCondition;
import com.tak.daocore.dao.impl.result.QueryResult;
import com.tak.daocore.model.Paging;

import java.util.List;
import java.util.Map;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:17
 */
public interface IBaseDao<T> {

    /**
     * 获取数据库操作对象
     */
    SQLiteOpenHelper getDbHelper();

    /**
     * 默认主键自增,调用insert(T,true);
     *
     * @param entity
     * @return
     */
    long save(T entity);

    /**
     * 插入实体类
     *
     * @param entity
     * @param flag   flag为true是自动生成主键,flag为false时需手工指定主键.
     * @return
     */
    long save(T entity, boolean flag);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(String id);

    /**
     * 删除多条数据
     *
     * @param ids
     */
    void delete(String... ids);

    /**
     * 删除所有数据
     */
    void deleteAll();

    /**
     * 更新实体类来
     *
     * @param entity
     */
    void update(T entity);

    /**
     * 根据ID来查找
     *
     * @param id
     * @return
     */
    T get(String id);

    /**
     * 查询当前数据
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    List<T> rawQuery(String sql, String[] selectionArgs);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据条件来查询
     *
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    List<T> find(String[] columns, String selection,
                 String[] selectionArgs, String groupBy, String having,
                 String orderBy, String limit);

    /**
     * 判断当前表是否存在
     * @return
     */
    boolean isTableExist();

    /**
     * 判断表是否存在
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    boolean isExist(String sql, String[] selectionArgs);

    /**
     * 将查询的结果保存为名值对map.
     *
     * @param sql           查询sql
     * @param selectionArgs 参数值
     * @return 返回的Map中的key全部是小写形式.
     */
    List<Map<String, String>> query2MapList(String sql, String[] selectionArgs);

    /**
     * 封装执行sql代码.
     *
     * @param sql
     * @param selectionArgs
     */
    void executeJPQL(String sql, Object[] selectionArgs);

    /**
     * 根据条件查询数据
     *
     * @param condition
     * @return
     * @throws DAOException
     */
    QueryResult<T> find(QueryCondition condition);

    /**
     * 根据条件查询数据
     *
     * @param namedWhereClause
     * @param nameOrderbyClause
     * @param paging
     * @param params
     * @return
     */
    QueryResult<T> find(String namedWhereClause, String nameOrderbyClause, Paging paging, Map<String, Object> params) throws DAOException;

    /**
     * 统一关闭数据库连接
     */
    void closeDB();
}
