package com.spboot.fooddelivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CART_ITEM")
public class CartItem {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Column(name = "QUANTITY")
	int quantity;

	@ManyToOne
	@JoinColumn(name="CART_ID")
	Cart cart;
	
	@ManyToOne
	@JoinColumn(name="RESTAURANT_ITEM_ID")
	RestaurantItem restaurantItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public RestaurantItem getRestaurantItem() {
		return restaurantItem;
	}

	public void setRestaurantItem(RestaurantItem restaurantItem) {
		this.restaurantItem = restaurantItem;
	}
	
}
