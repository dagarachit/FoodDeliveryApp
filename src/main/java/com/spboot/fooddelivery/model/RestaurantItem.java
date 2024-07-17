package com.spboot.fooddelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "RESTAURANT_ITEM")
public class RestaurantItem {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "RATING")
	private int rating;
	@Column(name = "PRICE")
	private float price;
	@Column(name = "IS_AVAILABLE")
	private boolean isAvailable;

	@OneToOne
	@JoinColumn(name = "FOOD_ITEM_ID")
	FoodItem foodItem;

	@ManyToOne
	@JoinColumn(name = "RESTAURANT_ID")
	private Restaurant restaurant;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@JsonIgnore
	public Restaurant getRestaurant() {
		return restaurant;
	}

	@JsonIgnore
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@JsonIgnore
	public FoodItem getFoodItem() {
		return foodItem;
	}

	@JsonIgnore
	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}

}
