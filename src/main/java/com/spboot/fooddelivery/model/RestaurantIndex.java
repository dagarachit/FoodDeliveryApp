package com.spboot.fooddelivery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Document(indexName = "restaurants")
public class RestaurantIndex {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Field(type = FieldType.Text)
    private String name;

//    @Field(type = FieldType.Object)
    @GeoPointField
    private GeoPoint location;

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

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}
    
    
}
