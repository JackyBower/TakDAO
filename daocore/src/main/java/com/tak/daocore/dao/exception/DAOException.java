package com.tak.daocore.dao.exception;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:21
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = 3205030017582837048L;

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}

