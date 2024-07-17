package com.spboot.fooddelivery.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import com.spboot.fooddelivery.dto.CreateOrderRequestDTO;
import com.spboot.fooddelivery.dto.CreateOrderResponseDTO;
import com.spboot.fooddelivery.dto.NearbyOpenRestaurantsResponseDTO;
import com.spboot.fooddelivery.dto.PastOrderResponseDTO;
import com.spboot.fooddelivery.dto.PastOrder_ItemDTO;
import com.spboot.fooddelivery.dto.PersonalizedFoodResponseDTO;
import com.spboot.fooddelivery.dto.SearchCriteriaRequestDTO;
import com.spboot.fooddelivery.dto.SearchItemsResponseDTO;
import com.spboot.fooddelivery.enums.OrderStatus;
import com.spboot.fooddelivery.exception.FoodDeliveryException;
import com.spboot.fooddelivery.model.Address;
import com.spboot.fooddelivery.model.Cart;
import com.spboot.fooddelivery.model.CartItem;
import com.spboot.fooddelivery.model.FoodItem;
import com.spboot.fooddelivery.model.Order;
import com.spboot.fooddelivery.model.OrderItem;
import com.spboot.fooddelivery.model.Restaurant;
import com.spboot.fooddelivery.model.RestaurantIndex;
import com.spboot.fooddelivery.model.RestaurantItem;
import com.spboot.fooddelivery.model.User;
import com.spboot.fooddelivery.repository.CartRepository;
import com.spboot.fooddelivery.repository.OrderItemRepository;
import com.spboot.fooddelivery.repository.OrderRepository;
import com.spboot.fooddelivery.repository.RestaurantElasticRepository;
import com.spboot.fooddelivery.repository.RestaurantItemRepository;
import com.spboot.fooddelivery.repository.RestaurantRepository;
import com.spboot.fooddelivery.repository.UserRepository;

@Service
public class ConsumerService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	RestaurantItemRepository itemRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	RestaurantElasticRepository restaurantElasticRepository;
	@Autowired
	RestaurantItemRepository restaurantItemRepository;

	// CUSTOMER ONLY
	public CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO)
			throws FoodDeliveryException {
		CreateOrderResponseDTO createOrderResponseDTO = new CreateOrderResponseDTO();
		Optional<Restaurant> restaurantOpt = restaurantRepository.findById(createOrderRequestDTO.getRestaurantId());
		Optional<User> consumerOpt = userRepository.findById(createOrderRequestDTO.getUserId());
		Optional<Cart> cartOpt = cartRepository.findById(createOrderRequestDTO.getCartId());
		if (cartOpt.isEmpty() || consumerOpt.isEmpty() || restaurantOpt.isEmpty()) {
			throw new FoodDeliveryException(
					"Please provide accurate CartID, ConsumerID or RestaurantID. One of these is incorrect.");
		}

		if (cartOpt.get().getRestaurant().getId() != createOrderRequestDTO.getRestaurantId()) {
			throw new FoodDeliveryException("The cart does not belong to this restaurant!!");
		}
		Order order = new Order();
		Set<OrderItem> orderItems = new HashSet<>();
		Set<CartItem> cartItems = cartOpt.get().getCartItems();
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setPrice(cartItem.getRestaurantItem().getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setFoodItem(cartItem.getRestaurantItem().getFoodItem());
			orderItem.setOrder(order);
			orderItemRepository.save(orderItem);
			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);
		order.setTotalPrice(createOrderRequestDTO.getTotalPrice());
		if (createOrderRequestDTO.getOrderStatus() == OrderStatus.ORDER_PLACED) {
			order.setOrderStatus(createOrderRequestDTO.getOrderStatus());
		} else {
			throw new FoodDeliveryException("Wrong Order Status. Please send status as : " + OrderStatus.ORDER_PLACED);
		}

		order.setUser(consumerOpt.get());
		order.setRestaurant(restaurantOpt.get());
		Order updatedOrder = orderRepository.save(order);
		Cart cart = cartRepository.findById(createOrderRequestDTO.getCartId()).get();
		cartRepository.delete(cart);
		createOrderResponseDTO.setId(updatedOrder.getId());
		createOrderResponseDTO.setOrderStatus(updatedOrder.getOrderStatus());
		createOrderResponseDTO.setTotalPrice(updatedOrder.getTotalPrice());
		return createOrderResponseDTO;
	}

	// CUSTOMER ONLY
	public OrderStatus checkOrderStatus(int orderId) throws FoodDeliveryException {
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isEmpty()) {
			throw new FoodDeliveryException("No Order Found for this OrderID : " + orderId);
		}
		return orderOpt.get().getOrderStatus();
	}

	// CUSTOMER ONLY
	public List<PastOrderResponseDTO> pastOrders(int consumerId, int pageNo, int pageSize) {
		// List helps maintaining retrieved objects in a sort manner
		List<PastOrderResponseDTO> pastOrderResponseDTOs = new ArrayList<PastOrderResponseDTO>();
		List<Order> orders = orderRepository.findAllByUserIdOrderByIdDesc(consumerId, PageRequest.of(pageNo, pageSize));
		for (Order order : orders) {
			PastOrderResponseDTO pastOrderResponseDTO = new PastOrderResponseDTO();
			Set<PastOrder_ItemDTO> itemDTOs = new HashSet<>();
			for (OrderItem orderItem : order.getOrderItems()) {
				PastOrder_ItemDTO itemDTO = new PastOrder_ItemDTO();
				itemDTO.setItemName(orderItem.getFoodItem().getName());
				itemDTO.setItemQuantity(orderItem.getQuantity());
				itemDTOs.add(itemDTO);
			}
			pastOrderResponseDTO.setOrderStatus(order.getOrderStatus());
			pastOrderResponseDTO.setItemDTOs(itemDTOs);
			pastOrderResponseDTO.setRestaurantAddressLine(order.getRestaurant().getAddress().getAddressLine());
			pastOrderResponseDTO.setRestaurantName(order.getRestaurant().getName());
			pastOrderResponseDTO.setTotalPrice(order.getTotalPrice());
			pastOrderResponseDTOs.add(pastOrderResponseDTO);
		}
		return pastOrderResponseDTOs;
	}

	// CUSTOMER ONLY
	public List<SearchItemsResponseDTO> restaurantFoodItemSearch(SearchCriteriaRequestDTO searchCriteriaDTO) throws FoodDeliveryException {
		//Pagination params : int pageNo,int pageSize
		// List helps maintaining retrieved objects in a sort manner
		List<SearchItemsResponseDTO> searchItemsResponseDTOs = new ArrayList<SearchItemsResponseDTO>();
		if (searchCriteriaDTO.getCuisine() == null && searchCriteriaDTO.getIsVeg() == null
				&& (("".equalsIgnoreCase(searchCriteriaDTO.getName()) || searchCriteriaDTO.getName() == null))
				&& (searchCriteriaDTO.getRating() < 1 || searchCriteriaDTO.getRating() > 5)) {
			throw new FoodDeliveryException("Please provide all Query parameters");
		}
		//get all restitems based on restID where restId=2 and isVeg={isVeg} and name={name} and rating={rating}
		List<RestaurantItem> restaurantItems = restaurantItemRepository.findAllByRestaurantIdOrderByRatingDesc(
				searchCriteriaDTO.getRestaurantId());//, PageRequest.of(pageNo, pageSize)
//		List<RestaurantItem> restaurantItems = restaurantItemRepository.findFilteredRestaurantItemsByNativeQuery(searchCriteriaDTO.getRestaurantId(),searchCriteriaDTO.getCuisine() , searchCriteriaDTO.getIsVeg(), searchCriteriaDTO.getRating(), PageRequest.of(pageNo, pageSize));
//		List<RestaurantItem> restaurantItems = restaurantItemRepository.findFilteredRestaurantItems(searchCriteriaDTO.getRestaurantId(),searchCriteriaDTO.getCuisine() , searchCriteriaDTO.getIsVeg(), searchCriteriaDTO.getRating(),searchCriteriaDTO.getName(), PageRequest.of(pageNo, pageSize));

		for (RestaurantItem restaurantItem : restaurantItems) {
			
			SearchItemsResponseDTO searchItemsResponseDTO = new SearchItemsResponseDTO();

			if (searchCriteriaDTO.getCuisine() != null) {
				if (! (searchCriteriaDTO.getCuisine() == restaurantItem.getFoodItem().getCuisine())) {
					continue;
				}
			}

			if (searchCriteriaDTO.getIsVeg() != null) {
				if (! (searchCriteriaDTO.getIsVeg() == restaurantItem.getFoodItem().getIsVeg())) {
					continue;
				}
			}

			if (!"".equalsIgnoreCase(searchCriteriaDTO.getName()) && searchCriteriaDTO.getName() != null) {
				if (! (searchCriteriaDTO.getName().equals(restaurantItem.getFoodItem().getName()) ) ) {
					continue;
				}
			}

			if (searchCriteriaDTO.getRating() >= 1 && searchCriteriaDTO.getRating() <= 5) {
				if (!(restaurantItem.getRating() >= searchCriteriaDTO.getRating())) {
					continue;
				}
			}

			searchItemsResponseDTO.setItemName(restaurantItem.getFoodItem().getName());
			searchItemsResponseDTO.setRating(restaurantItem.getRating());
			searchItemsResponseDTO.setPrice(restaurantItem.getPrice());
			searchItemsResponseDTO.setDescription(restaurantItem.getFoodItem().getDescription());
			searchItemsResponseDTOs.add(searchItemsResponseDTO);

		}
		return searchItemsResponseDTOs;

	}

	// CUSTOMER ONLY
	//Assumption of personalization of login in readme. Output will be list of restaurants
	public List<PersonalizedFoodResponseDTO> personalizedFoodSuggestion(int userId, int pageNo, int pageSize)
			throws FoodDeliveryException {
		// List helps maintaining retrieved objects in a sort manner
		List<PersonalizedFoodResponseDTO> personalizedFoodResponseDTOs = new ArrayList<>();
		List<OrderItem> orderItems = orderItemRepository.findPersonalizedItemsForUser(userId,
				PageRequest.of(pageNo, pageSize));
		
		Map<Restaurant, List<OrderItem>> map = orderItems.stream().collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getRestaurant() ));
		Set<Entry<Restaurant,List<OrderItem> >> entrySet = map.entrySet();
		
		for(Entry<Restaurant,List<OrderItem>> entry : entrySet) {
			Map<FoodItem, Integer> foodItemAndCountMap = new LinkedHashMap<>();
			for (OrderItem orderItem :  entry.getValue()) {
				int count = 0;
				if (foodItemAndCountMap.containsKey(orderItem.getFoodItem())) {
					count = foodItemAndCountMap.get(orderItem.getFoodItem());
				}
				count++;
				foodItemAndCountMap.put(orderItem.getFoodItem(), count);
			}
			foodItemAndCountMap = foodItemAndCountMap.entrySet().stream()
					.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			
			Entry<FoodItem, Integer> foodItemEntry = foodItemAndCountMap.entrySet().stream().findFirst().get();
			RestaurantItem restItem = itemRepository.findByFoodItem_Id(foodItemEntry.getKey().getId()).get(0);
			PersonalizedFoodResponseDTO personalizedFoodResponseDTO = new PersonalizedFoodResponseDTO();
			personalizedFoodResponseDTO.setItemName(restItem.getFoodItem().getName());
			personalizedFoodResponseDTO.setItemRate(restItem.getPrice());
			personalizedFoodResponseDTO.setItemRating(restItem.getRating());
			personalizedFoodResponseDTO.setRestaurantId(entry.getKey().getId());
			personalizedFoodResponseDTO.setRestaurantItemId(restItem.getId());
			personalizedFoodResponseDTO.setRestaurantName(entry.getKey().getName());
			personalizedFoodResponseDTOs.add(personalizedFoodResponseDTO);
		}
		
		return personalizedFoodResponseDTOs;
	}

	// CUSTOMER ONLY
	// Providing the best rated item of the restaurant.
	public List<NearbyOpenRestaurantsResponseDTO> browseRestaurants(int consumerId, int pageNo, int pageSize)
			throws FoodDeliveryException {
		// List helps maintaining retrieved objects in a sort manner
		List<NearbyOpenRestaurantsResponseDTO> nearbyOpenRestaurantsResponseDTOs = new ArrayList<>();

		Optional<User> consumerOpt = userRepository.findById(consumerId);
		User consumer = null;
		if (consumerOpt.isEmpty()) {
			throw new FoodDeliveryException("Consumer DOES NOT EXIST");
		} else {
			consumer = consumerOpt.get();
		}

		Address consumerAddress = consumer.getAddress();
		if (consumerAddress == null) {
			throw new FoodDeliveryException("Consumer DOES NOT have an Address. Please check the consumer Id sent");
		}
		List<RestaurantIndex> restaurantIndexes = restaurantElasticRepository.findByLocationNear(
				new GeoPoint(consumerAddress.getLatitude(), consumerAddress.getLongitude()), "4km",
				PageRequest.of(pageNo, pageSize));
		for (RestaurantIndex restaurantIndex : restaurantIndexes) {
			int restaurantId = restaurantIndex.getId();
			Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
			LocalTime currentTime = LocalTime.now();
			if (currentTime.compareTo(restaurant.getStartTime()) > 0
					&& currentTime.compareTo(restaurant.getEndTime()) < 0) {
				NearbyOpenRestaurantsResponseDTO nearbyOpenRestaurantsResponseDTO = new NearbyOpenRestaurantsResponseDTO();
				nearbyOpenRestaurantsResponseDTO.setRestaurantName(restaurant.getName());
				nearbyOpenRestaurantsResponseDTO.setCuisine(restaurant.getRestaurantItems().stream()
						.map(a -> a.getFoodItem()).map(foodItem -> foodItem.getCuisine()).collect(Collectors.toSet()));
				nearbyOpenRestaurantsResponseDTO.setDistance(calculateDistance(restaurantIndex.getLocation(),
						new GeoPoint(consumerAddress.getLatitude(), consumerAddress.getLongitude())));

				// Adding first item and rating.
				nearbyOpenRestaurantsResponseDTO
						.setRating(!restaurant.getRestaurantItems().isEmpty() ? restaurant.getRestaurantItems().stream()
								.map(item -> item.getRating()).sorted(Collections.reverseOrder()).findFirst().get()
								: 0);
				nearbyOpenRestaurantsResponseDTO.setItemName(!restaurant.getRestaurantItems().isEmpty()
						? restaurant.getRestaurantItems().stream().sorted((i1, i2) -> i2.getRating() - i1.getRating())
								.map(item -> item.getFoodItem().getName()).findFirst().get()
						: "");
				nearbyOpenRestaurantsResponseDTOs.add(nearbyOpenRestaurantsResponseDTO);
			}
		}
		return nearbyOpenRestaurantsResponseDTOs;
	}

	private static int calculateDistance(GeoPoint geoPoint1, GeoPoint geoPoint2) {
		//Havershine formula.
		int EARTH_RADIUS = 6371;
		double latitude1Radians = Math.toRadians(geoPoint1.getLat());
		double latitude2Radians = Math.toRadians(geoPoint1.getLon());
		double longitude1Radians = Math.toRadians(geoPoint2.getLat());
		double longitude2Radians = Math.toRadians(geoPoint2.getLon());
		
		 // Calculate the differences between the coordinates
        double deltaLatitude = latitude2Radians - latitude1Radians;
        double deltaLongitude = longitude2Radians - longitude1Radians;

        // Apply the Haversine formula
        double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
                   Math.cos(latitude1Radians) * Math.cos(latitude2Radians) *
                   Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        return (int) (EARTH_RADIUS * c);
	}
}
