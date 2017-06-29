package com.tak.daocore.dao.impl.query;

/**
 * Desc：查询运算符
 * User: ZhouJing
 * Date: 2017/6/28 15:21
 */
public enum QueryOperator {

    EQ(" = "),
    NE(" <> "),
    GT(" > "),
    LT(" < "),
    GE(" >= "),
    LE(" <= "),
    BETWEEN(" BETWEEN "),
    LIKE(" LIKE "),
    NOTIN(" NOT IN "),
    IN(" IN "),
    IS(" IS NULL "),
    ISNOT(" IS NOT NULL ");

    private final String opt;

    public String getOpt() {
        return this.opt;
    }

    private QueryOperator(String opt) {
        this.opt = opt;
    }

}
