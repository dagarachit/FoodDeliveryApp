package com.spboot.fooddelivery.dto;

import com.spboot.fooddelivery.enums.OrderStatus;

public class CreateOrderResponseDTO {

	private int id;
	private float totalPrice;
	private OrderStatus orderStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

}
