package com.tak.daocore.dao.impl.result;

import java.io.Serializable;
import java.util.List;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:21
 */
public class QueryResult<T> implements Serializable {

    private static final long serialVersionUID = -4877730951379704875L;
    private List<T> rows;
    private long total;

    public QueryResult() {
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
