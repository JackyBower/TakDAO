package com.tak.dao.dao;

import com.tak.dao.entity.User;
import com.tak.daocore.dao.IBaseDao;

import java.util.List;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 18:17
 */
public interface IUserDao extends IBaseDao<User>{

    List<User> findUserByList(String id,String name);
}
