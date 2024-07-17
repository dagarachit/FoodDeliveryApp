package com.spboot.fooddelivery.model;

import java.time.LocalTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="RESTAURANT")
public class Restaurant {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank
	@Column(name = "NAME")
	private String name;
	@Column(name = "START_TIME")
	private LocalTime startTime;
	@Column(name = "END_TIME")
	private LocalTime endTime;

	@OneToOne(cascade=CascadeType.ALL,mappedBy="restaurant")
	private Address address;
	
	@OneToOne(cascade=CascadeType.ALL,mappedBy="restaurant")
	private ContactInfo contactInfo ;
////	cascade= {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}
	
	// Want to remove items on removing RestaurantOwner so CascadeType.ALL used
	@OneToMany(mappedBy = "restaurant")
	private Set<RestaurantItem> restaurantItems;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	
	// Don't want to remove orders on removing RestaurantOwner
//	@OneToMany(mappedBy = "restaurant", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
//			CascadeType.REFRESH,CascadeType.REMOVE })
//	private Set<Order> order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	@JsonIgnore
	public Address getAddress() {
		return address;
	}
	@JsonIgnore
	public void setAddress(Address address) {
		this.address = address;
	}
	@JsonIgnore
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	@JsonIgnore
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

//	@JsonIgnore
//	public Set<Order> getOrder() {
//		return order;
//	}
//
//	@JsonIgnore
//	public void setOrder(Set<Order> order) {
//		this.order = order;
//	}

	@JsonIgnore
	public Set<RestaurantItem> getRestaurantItems() {
		return restaurantItems;
	}

	@JsonIgnore
	public void setRestaurantItems(Set<RestaurantItem> restaurantItems) {
		this.restaurantItems = restaurantItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
