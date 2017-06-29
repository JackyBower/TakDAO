package com.tak.daocore.dao.impl.query;

/**
 * Desc：查询逻辑
 * User: ZhouJing
 * Date: 2017/6/28 15:22
 */
public enum QueryLogic {

    AND(" AND "),
    OR(" OR ");

    private final String logic;

    public String getLogic() {
        return this.logic;
    }

    private QueryLogic(String logic) {
        this.logic = logic;
    }

}
