package com.spboot.fooddelivery.dto;

import java.time.LocalTime;

public class UpdateRestaurantTimingRequestDTO {

	private int id;
	private LocalTime startTime;
	private LocalTime endTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	
	
}
