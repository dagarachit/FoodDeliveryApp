package com.spboot.fooddelivery.exception;

public class FoodDeliveryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	 
	public FoodDeliveryException(String message) {
		super(message);
		this.message = message;
	}
	public FoodDeliveryException() {
		super();
	}

}
