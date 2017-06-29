package com.tak.dao.entity;

import com.tak.daocore.annotation.Column;
import com.tak.daocore.annotation.Table;
import com.tak.daocore.entity.DefaultEntity;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 18:03
 */
@Table(name = "hw_user")
public class User extends DefaultEntity{

    /**
     * 名称
     */
    @Column(name = "name", length = 100)
    private String name;

    /**
     * 状态(0代表未激活,1代表激活)
     */
    @Column(name = "status", length = 20)
    private String status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
