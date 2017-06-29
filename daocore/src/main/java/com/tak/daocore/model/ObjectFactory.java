package com.tak.daocore.model;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:13
 */
public class ObjectFactory {

    public static IQueryCondition createQueryCondition() {
        return new IQueryConditionImpl();
    }
}
