package com.spboot.fooddelivery.dto;

import java.util.Set;

import com.spboot.fooddelivery.enums.OrderStatus;

public class PastOrderResponseDTO {
	private String restaurantName;
	private OrderStatus orderStatus;
	private float totalPrice;
	private String restaurantAddressLine;
	private Set<PastOrder_ItemDTO> itemDTOs;

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getRestaurantAddressLine() {
		return restaurantAddressLine;
	}

	public void setRestaurantAddressLine(String restaurantAddressLine) {
		this.restaurantAddressLine = restaurantAddressLine;
	}

	public Set<PastOrder_ItemDTO> getItemDTOs() {
		return itemDTOs;
	}

	public void setItemDTOs(Set<PastOrder_ItemDTO> itemDTOs) {
		this.itemDTOs = itemDTOs;
	}

}
