package com.tak.daocore.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Desc：查询条件接口类
 * User: ZhouJing
 * Date: 2017/6/28 15:05
 */
public interface IQueryCondition extends Serializable {

    /**
     * 添加查询条件
     *
     * @param conditionName
     * @param conditionValue
     */
    void addQueryCondition(String conditionName, String conditionValue);

    /**
     * 按列表值添加查询条件
     *
     * @param conditionName
     * @param conditionValue
     */
    void addQueryConditionByListValue(String conditionName, List<Object> conditionValue);

    /**
     * 获取查询条件
     *
     * @param conditionName
     * @return
     */
    String getQueryCondition(String conditionName);

    /**
     * 获取页面大小
     */
    int getPageSize();

    /**
     * 设置页面大小
     *
     * @param pageSize
     */
    void setPageSize(int pageSize);


    /**
     * 获取页面索引
     */
    int getPageIndex();


    /**
     * 设置页面索引
     *
     * @param pageIndex
     */
    void setPageIndex(int pageIndex);

    /**
     * 获取所有查询条件
     */
    Map<String, Object> getAllQueryConditions();

    /**
     * 获取排序字段
     */
    String getSort();

    /**
     * 设置排序字段
     *
     * @param sort
     */
    void setSort(String sort);

    /**
     * 获取排序方向
     */
    String getDirection();

    /**
     * 设置排序方向(DESC/ASC)
     *
     * @param direction
     */
    void setDirection(String direction);
}
