package com.mo9.rabbitmq;

import java.io.Serializable;

public class Content implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9192839621910564575L;
	
	
	private String orderCode;
	private String userCode;
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	

}
