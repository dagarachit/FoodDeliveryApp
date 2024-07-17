package com.spboot.fooddelivery.dto;

public class PersonalizedFoodResponseDTO {

	private String itemName;
	private Float itemRate;
	private Integer itemRating;
	private String restaurantName;
	private int restaurantId;
	private int restaurantItemId;


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Float getItemRate() {
		return itemRate;
	}

	public void setItemRate(Float itemRate) {
		this.itemRate = itemRate;
	}

	public Integer getItemRating() {
		return itemRating;
	}

	public void setItemRating(Integer itemRating) {
		this.itemRating = itemRating;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getRestaurantItemId() {
		return restaurantItemId;
	}

	public void setRestaurantItemId(int restaurantItemId) {
		this.restaurantItemId = restaurantItemId;
	}
	

}
