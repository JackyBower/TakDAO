package com.tak.daocore.model;

import java.io.Serializable;

/**
 * Desc：分页实体
 * User: ZhouJing
 * Date: 2017/6/28 15:13
 */
public class Paging implements Serializable {

    private static final long serialVersionUID = 7839678214854912971L;

    /**
     * 当前页码，但没有记录号码
     */
    private int currentPage = 1;

    /**
     * 总页数
     */
    private int totalPage = 0;

    /**
     * 显示多少条记录
     */
    private int pageSize = 20;

    /**
     * 总纪录数
     */
    private int totalRecord = 0;

    public Paging() {
    }

    public Paging(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public Paging(int currentPage, int pageSize) {
        if (currentPage > 0) {
            this.currentPage = currentPage;
        }

        if (pageSize > 0) {
            this.pageSize = pageSize;
        }

    }

    public int getStartIndex() {
        return this.pageSize * (this.currentPage - 1);
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage > 0) {
            this.currentPage = currentPage;
        }
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public int getTotalRecord() {
        return this.totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

}
