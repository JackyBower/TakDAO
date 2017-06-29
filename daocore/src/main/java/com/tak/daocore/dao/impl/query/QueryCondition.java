package com.tak.daocore.dao.impl.query;


import com.tak.daocore.dao.exception.QueryException;
import com.tak.daocore.dao.impl.query.param.QueryParam;
import com.tak.daocore.model.Paging;
import com.tak.daocore.utils.StringUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:22
 */
public class QueryCondition implements Serializable {

    private static final long serialVersionUID = 4329430091781131501L;
    private Paging paging = new Paging();
    private String orderBy;
    private QueryCondition.DIRECTION direction;
    private LinkedHashMap<String, DIRECTION> orderBys;
    private QueryParam param;

    public QueryCondition() {
    }

    public String getQueryClause() throws QueryException {
        String whereClause = this.param == null ? "" : this.param.getQueryClause();
        StringBuilder clause = StringUtils.isBlank(whereClause) ? new StringBuilder() : new StringBuilder(whereClause);
        return clause.toString();
    }

    public String getQueryOrderBy() {
        StringBuilder clause = new StringBuilder();
        //Android数据库不支持ORDER BY
        if (this.orderBys != null && this.orderBys.size() > 0) {
            clause.append(" ");
//            clause.append(" ORDER BY ");
            Iterator i$ = this.orderBys.entrySet().iterator();

            while (i$.hasNext()) {
                Map.Entry order = (Map.Entry) i$.next();
                clause.append((String) order.getKey()).append(" ");
//                clause.append("entity.").append((String) order.getKey()).append(" ");
                if (order.getValue() != null) {
                    clause.append(order.getValue()).append(",");
                }
            }
            clause.deleteCharAt(clause.length() - 1);
        } else if (StringUtils.isNotBlank(this.orderBy)) {
            clause.append(" ").append(this.orderBy);
//            clause.append(" ORDER BY ").append("entity.").append(this.orderBy);
            if (this.direction != null) {
                clause.append(" ").append(this.direction);
            }
        }
        return clause.toString();
    }

    public Paging getPaging() {
        return this.paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrderBy(String orderBy, QueryCondition.DIRECTION direction) {
        this.orderBy = orderBy;
        this.direction = direction;
    }

    public QueryCondition.DIRECTION getDirection() {
        return this.direction;
    }

    public void setDirection(QueryCondition.DIRECTION direction) {
        this.direction = direction;
    }

    public LinkedHashMap<String, DIRECTION> getOrderBys() {
        return this.orderBys;
    }

    public void setOrderBys(LinkedHashMap<String, DIRECTION> orderBys) {
        this.orderBys = orderBys;
    }

    public QueryParam getParam() {
        return this.param;
    }

    public void setParam(QueryParam param) {
        this.param = param;
    }

    public static enum DIRECTION {
        ASC,
        DESC;

        private DIRECTION() {

        }
    }
}
