package com.tak.daocore.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc：查询条件实现类
 * User: ZhouJing
 * Date: 2017/6/28 15:12
 */
public class IQueryConditionImpl implements IQueryCondition, Serializable {

    Map<String, Object> allQueryConditions = new HashMap<String, Object>();

    int pageSize;

    int pageIndex = 1;

    private String sort;

    private String direction;

    public String getQueryCondition(String conditionName) {
        String ret = null;
        try {
            Object temp = allQueryConditions.get(conditionName);
            if (temp instanceof QueryClause) {
                ret = (String) ((QueryClause) temp).getValue();
            } else {
                ret = (String) allQueryConditions.get(conditionName);
            }
        } catch (Exception ex) {
            ret = "";
        }
        return ret;
    }

    @Override
    public void addQueryCondition(String conditionName, String conditionValue) {
        if (conditionValue != null) {
            conditionValue = conditionValue.trim();
            conditionValue = conditionValue.replaceAll("'", "''");
        }
        allQueryConditions.put(conditionName, conditionValue);
    }

    @Override
    public void addQueryConditionByListValue(String conditionName, List<Object> conditionValue) {
        allQueryConditions.put(conditionName, conditionValue);
    }

    @Override
    public Map<String, Object> getAllQueryConditions() {
        return allQueryConditions;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public String getSort() {
        return sort;
    }

    @Override
    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    @Override
    public void setDirection(String direction) {
        this.direction = direction;
    }
}
