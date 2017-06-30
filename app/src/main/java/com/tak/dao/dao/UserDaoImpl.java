package com.tak.dao.dao;

import android.content.Context;

import com.tak.dao.entity.User;
import com.tak.daocore.dao.impl.BaseDaoImpl;
import com.tak.daocore.db.DBHelper;

import java.util.List;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 18:18
 */
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {

    public UserDaoImpl(Context context) {
        //通过DBHelper.getInstance()获取getSQLiteDBHelper()对象
        super(DBHelper.getInstance().getSQLiteDBHelper(context), User.class);
    }

    @Override
    public List<User> findUserByList(String id, String name) {
        //实现复杂的业务逻辑
        return null;
    }
}
