package com.tak.daocore.model;

import java.io.Serializable;

/**
 * Desc：查询条件
 * User: ZhouJing
 * Date: 2017/6/28 15:14
 */
public class QueryClause implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String AND = " AND ";
    public static final String OR = " OR ";
    public static final String SPACE = " ";
    public static final String LIKE = " LIKE ";
    public static final String L_LIKE = "llike";
    public static final String R_LIKE = "rlike";
    public static final String EQUAL = " = ";
    public static final String INEQUALITY = " <> ";

    private String logic;
    private String name;
    private String opt;
    private Object value;

    public QueryClause() {
    }

    public QueryClause(String logic, String name, String opt, Object value) {
        this.logic = logic;
        this.name = name;
        this.opt = opt;
        this.value = value;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append(logic).append(String.format("%s", name)).append(SPACE).append(opt).append(SPACE).append(":").append(name.replaceAll("\\.", ""));
        return sql.toString();
    }
}