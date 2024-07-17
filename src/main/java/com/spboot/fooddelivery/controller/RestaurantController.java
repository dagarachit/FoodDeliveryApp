package com.spboot.fooddelivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spboot.fooddelivery.dto.CreateRestaurantItemRequestDTO;
import com.spboot.fooddelivery.dto.CreateRestaurantItemResponseDTO;
import com.spboot.fooddelivery.dto.CreateRestaurantRequestDTO;
import com.spboot.fooddelivery.dto.CreateRestaurantResponseDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantRequestDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantResponseDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantTimingRequestDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantTimingResponseDTO;
import com.spboot.fooddelivery.enums.OrderStatus;
import com.spboot.fooddelivery.exception.FoodDeliveryException;
import com.spboot.fooddelivery.model.RestaurantIndex;
import com.spboot.fooddelivery.model.RestaurantItem;
import com.spboot.fooddelivery.service.RestaurantService;

@ControllerAdvice
@RestController()
@RequestMapping(value="/api/v1/restaurants", produces={MediaType.APPLICATION_JSON_VALUE})
public class RestaurantController {

	@Autowired
	RestaurantService restaurantService;

	// Restaurant Creation, Updation and Deletion API's Start
	@PostMapping("/")
	public CreateRestaurantResponseDTO createRestaurant(@RequestBody CreateRestaurantRequestDTO createRestaurantRequestDTO) throws FoodDeliveryException {
		return restaurantService.createRestaurant(createRestaurantRequestDTO);
	}

	@PutMapping("/")
	public UpdateRestaurantResponseDTO updateRestaurant(@RequestBody UpdateRestaurantRequestDTO updateRestaurantRequestDTO) {
		return restaurantService.updateRestaurant(updateRestaurantRequestDTO);
	}

	@DeleteMapping("/{id}")
	public int deleteRestaurant(@PathVariable int id) {
		return restaurantService.deleteRestaurant(id);
	}
	// Restaurant Creation, Updation and Deletion API's End

	// Restaurant Timing Update
	@PatchMapping("/updateTimings")
	public UpdateRestaurantTimingResponseDTO updateRestaurantTiming(@RequestBody UpdateRestaurantTimingRequestDTO updateRestaurantTimingRequestDTO) throws FoodDeliveryException {
		return restaurantService.updateRestaurantTiming(updateRestaurantTimingRequestDTO);
	}

	// ITEMS Creation, Updation and Deletion API's Start
	@PostMapping("/item")
	public CreateRestaurantItemResponseDTO addItem(CreateRestaurantItemRequestDTO createRestaurantItemRequestDTO) throws FoodDeliveryException {
		return restaurantService.addItem(createRestaurantItemRequestDTO);
	}

	@PatchMapping("/item/{itemId}/available/{isAvailable}")
	public int updateItemAvailability(@PathVariable int itemId, @PathVariable boolean isAvailable) {
		return restaurantService.updateItemAvailability(itemId, isAvailable);
	}

	@PatchMapping("/item/{itemId}/price/{price}")
	public RestaurantItem updateItemPrice(@PathVariable int itemId, @PathVariable float price) {
		return restaurantService.updateItemPrice(itemId, price);
	}

	@DeleteMapping("/item/{restaurantItemId}")
	public int deleteItem(@PathVariable int restaurantItemId) {
		return restaurantService.deleteItem(restaurantItemId);
	}

	@PatchMapping("/{restaurantId}/order/{orderId}/approve")
	public OrderStatus approveOrderStatus(@PathVariable int orderId,@PathVariable int restaurantId) throws FoodDeliveryException {
		return restaurantService.approveOrderStatus(orderId,restaurantId);
	}

	@PatchMapping("/{restaurantId}/order/{orderId}/status/{orderStatus}")
	public OrderStatus markStatusChange(@PathVariable int orderId,@PathVariable int restaurantId,@PathVariable OrderStatus orderStatus) throws FoodDeliveryException {
		return restaurantService.markStatusChange(orderId,restaurantId,orderStatus);
	}

}
