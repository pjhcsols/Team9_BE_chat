package com.helpmeCookies.product.entity;

public enum OrderStatus {
	ORDER("핀매 중"),
	DONE("거래 완료"),
	RESERVED("예약 중");

	private final String orderStatusType;

	OrderStatus(String orderStatusType) {
		this.orderStatusType = orderStatusType;
	}

	public String getOrderStatusType() {
		return orderStatusType;
	}
}
