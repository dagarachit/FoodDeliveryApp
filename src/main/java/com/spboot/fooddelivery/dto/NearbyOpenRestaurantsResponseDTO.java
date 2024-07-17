package com.spboot.fooddelivery.dto;

import java.util.Set;

import com.spboot.fooddelivery.enums.Cuisine;

public class NearbyOpenRestaurantsResponseDTO {

	int rating;
	String itemName;
	String restaurantName;
	Set<Cuisine> cuisine;
	int distance;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public Set<Cuisine> getCuisine() {
		return cuisine;
	}

	public void setCuisine(Set<Cuisine> cuisine) {
		this.cuisine = cuisine;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
