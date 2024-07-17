package com.spboot.fooddelivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spboot.fooddelivery.dto.CreateOrderRequestDTO;
import com.spboot.fooddelivery.dto.CreateOrderResponseDTO;
import com.spboot.fooddelivery.dto.NearbyOpenRestaurantsResponseDTO;
import com.spboot.fooddelivery.dto.PastOrderResponseDTO;
import com.spboot.fooddelivery.dto.PersonalizedFoodResponseDTO;
import com.spboot.fooddelivery.dto.SearchCriteriaRequestDTO;
import com.spboot.fooddelivery.dto.SearchItemsResponseDTO;
import com.spboot.fooddelivery.enums.OrderStatus;
import com.spboot.fooddelivery.exception.FoodDeliveryException;
import com.spboot.fooddelivery.service.ConsumerService;

@RestController
@RequestMapping(value="/api/v1/consumers", produces={MediaType.APPLICATION_JSON_VALUE})
public class ConsumerController {

	@Autowired
	ConsumerService consumerService;

	@PostMapping("/order")
	public CreateOrderResponseDTO createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO) throws FoodDeliveryException {
		return consumerService.createOrder(createOrderRequestDTO);
	}
	
	@GetMapping("/order/status/{orderId}")
	public OrderStatus checkOrderStatus(@PathVariable int orderId) throws FoodDeliveryException {
		return consumerService.checkOrderStatus(orderId);
	}
	
	@GetMapping("/order/pastOrder/{consumerId}/pageNo/{pageNo}/pageSize/{pageSize}")
	public List<PastOrderResponseDTO> pastOrders(@PathVariable int consumerId,@PathVariable int pageNo,@PathVariable int pageSize) {
		return consumerService.pastOrders(consumerId, pageNo, pageSize);
	}
	
	// ITEMS Creation, Updation and Deletion API's Start
	@PostMapping("/filter")
	public List<SearchItemsResponseDTO> restaurantFoodItemSearch(SearchCriteriaRequestDTO searchCriteriaDTO) throws FoodDeliveryException {// Pagination Params : @PathVariable int pageNo,@PathVariable int pageSize
		return consumerService.restaurantFoodItemSearch(searchCriteriaDTO);//Pagination Params : pageNo, pageSize
	}

	@GetMapping("/personalized/{consumerId}/pageNo/{pageNo}/pageSize/{pageSize}")
	public List<PersonalizedFoodResponseDTO> personalizedFoodSuggestion(@PathVariable int consumerId,@PathVariable int pageNo,@PathVariable int pageSize) throws FoodDeliveryException {
		return consumerService.personalizedFoodSuggestion(consumerId, pageNo, pageSize);
	}
	
	@GetMapping("/{consumerId}/restaurant/browse/pageNo/{pageNo}/pageSize/{pageSize}")
	public List<NearbyOpenRestaurantsResponseDTO> browseRestaurants(@PathVariable int consumerId,@PathVariable int pageNo,@PathVariable int pageSize) throws FoodDeliveryException {
		return consumerService.browseRestaurants(consumerId, pageNo, pageSize);
	}
	
}
