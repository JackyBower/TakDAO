package com.tak.daocore.entity;


import com.tak.daocore.annotation.Column;
import com.tak.daocore.annotation.Id;

import java.io.Serializable;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:05
 */
public class DefaultEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6726760007021089302L;

	//主键
	@Id
	@Column(name = "id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
