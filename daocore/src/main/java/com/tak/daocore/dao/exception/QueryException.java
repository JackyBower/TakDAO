package com.tak.daocore.dao.exception;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:21
 */
public class QueryException extends DAOException {

    private static final long serialVersionUID = -7582756824068667032L;

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }
}
