package com.spboot.fooddelivery.service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import com.spboot.fooddelivery.auth.AuthService;
import com.spboot.fooddelivery.dto.CreateRestaurantItemRequestDTO;
import com.spboot.fooddelivery.dto.CreateRestaurantItemResponseDTO;
import com.spboot.fooddelivery.dto.CreateRestaurantRequestDTO;
import com.spboot.fooddelivery.dto.CreateRestaurantResponseDTO;
import com.spboot.fooddelivery.dto.RegisterDto;
import com.spboot.fooddelivery.dto.UpdateRestaurantRequestDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantResponseDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantTimingRequestDTO;
import com.spboot.fooddelivery.dto.UpdateRestaurantTimingResponseDTO;
import com.spboot.fooddelivery.enums.OrderStatus;
import com.spboot.fooddelivery.exception.FoodDeliveryException;
import com.spboot.fooddelivery.model.Address;
import com.spboot.fooddelivery.model.Cart;
import com.spboot.fooddelivery.model.ContactInfo;
import com.spboot.fooddelivery.model.FoodItem;
import com.spboot.fooddelivery.model.Order;
import com.spboot.fooddelivery.model.Restaurant;
import com.spboot.fooddelivery.model.RestaurantIndex;
import com.spboot.fooddelivery.model.RestaurantItem;
import com.spboot.fooddelivery.model.User;
import com.spboot.fooddelivery.repository.AddressRepository;
import com.spboot.fooddelivery.repository.CartItemRepository;
import com.spboot.fooddelivery.repository.CartRepository;
import com.spboot.fooddelivery.repository.ContactInfoRepository;
import com.spboot.fooddelivery.repository.FoodItemRepository;
import com.spboot.fooddelivery.repository.OrderRepository;
import com.spboot.fooddelivery.repository.RestaurantElasticRepository;
import com.spboot.fooddelivery.repository.RestaurantItemRepository;
import com.spboot.fooddelivery.repository.RestaurantRepository;
import com.spboot.fooddelivery.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class RestaurantService {

	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	ContactInfoRepository contactInfoRepository;
	@Autowired
	FoodItemRepository foodItemRepository;
	@Autowired
	RestaurantItemRepository restaurantItemRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	RestaurantElasticRepository restaurantElasticRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CartItemRepository cartItemRepository;
	@Autowired
	AuthService authService;

//	// Testing purpose
//	public List<Restaurant> getAllRestaurant() {
//		return restaurantRepository.findAll();
//	}
	@Transactional
	// Restaurant ADMIN ONLY
	public CreateRestaurantResponseDTO createRestaurant(CreateRestaurantRequestDTO createRestaurantRequestDTO)
			throws FoodDeliveryException {
		CreateRestaurantResponseDTO createRestaurantResponseDTO = new CreateRestaurantResponseDTO();
		Restaurant restaurant = new Restaurant();
		restaurant.setName(createRestaurantRequestDTO.getName());
		restaurant.setStartTime(createRestaurantRequestDTO.getStartTime());
		restaurant.setEndTime(createRestaurantRequestDTO.getEndTime());

		Address address = new Address();
		if (!(createRestaurantRequestDTO.getAddressLine() == null && createRestaurantRequestDTO.getCity() == null
				&& createRestaurantRequestDTO.getState() == null && createRestaurantRequestDTO.getCountry() == null
				&& createRestaurantRequestDTO.getPincode() == null && createRestaurantRequestDTO.getLatitude() == null
				&& createRestaurantRequestDTO.getLongitude() == null)) {
			address.setAddressLine(createRestaurantRequestDTO.getAddressLine());
			address.setCity(createRestaurantRequestDTO.getCity());
			address.setCountry(createRestaurantRequestDTO.getCountry());
			address.setLatitude(createRestaurantRequestDTO.getLatitude());
			address.setLongitude(createRestaurantRequestDTO.getLongitude());
			address.setPincode(createRestaurantRequestDTO.getPincode());
			address.setState(createRestaurantRequestDTO.getState());
		} else {
			address = null;
		}
		ContactInfo contactInfo = new ContactInfo();
		if (!(createRestaurantRequestDTO.getEmail() == null && createRestaurantRequestDTO.getPhoneNumber() == null)) {
			contactInfo.setEmail(createRestaurantRequestDTO.getEmail());
			contactInfo.setPhoneNumber(createRestaurantRequestDTO.getPhoneNumber());
			restaurant.setContactInfo(contactInfo);
		} else {
			contactInfo = null;
		}
		restaurant.setName(createRestaurantRequestDTO.getName());
		restaurant.setName(createRestaurantRequestDTO.getName());
		restaurant.setName(createRestaurantRequestDTO.getName());
		// Restaurant being created by ADMIN, adding owner_id.
		Optional<User> user = userRepository.findById(createRestaurantRequestDTO.getUserId());
		if (user.isEmpty()) {
			throw new FoodDeliveryException("User Not Found for userId : " + createRestaurantRequestDTO.getUserId());
		} else {
			restaurant.setUser(user.get());
		}
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		if (address != null) {
			address.setRestaurant(savedRestaurant);
			RestaurantIndex restaurantIndex = new RestaurantIndex();
			restaurantIndex.setLocation(new GeoPoint(address.getLatitude(), address.getLongitude()));
			restaurantIndex.setName(restaurant.getName());
			restaurantIndex.setId(restaurant.getId());
			restaurantElasticRepository.save(restaurantIndex);
			addressRepository.save(address);

		}
		if (contactInfo != null) {
			contactInfo.setRestaurant(savedRestaurant);
			contactInfoRepository.save(contactInfo);
		}
		createRestaurantResponseDTO.setId(savedRestaurant.getId());
		createRestaurantResponseDTO.setStartTime(savedRestaurant.getStartTime());
		createRestaurantResponseDTO.setEndTime(savedRestaurant.getEndTime());
		createRestaurantResponseDTO.setName(savedRestaurant.getName());
		if (savedRestaurant.getContactInfo() != null && savedRestaurant.getContactInfo().getEmail() != null)
			createRestaurantResponseDTO.setEmail(savedRestaurant.getContactInfo().getEmail());
		if (savedRestaurant.getContactInfo() != null && savedRestaurant.getContactInfo().getPhoneNumber() != null)
			createRestaurantResponseDTO.setPhoneNumber(savedRestaurant.getContactInfo().getPhoneNumber());

		return createRestaurantResponseDTO;
	}

	// Restaurant ADMIN ONLY
	public UpdateRestaurantResponseDTO updateRestaurant(UpdateRestaurantRequestDTO updateRestaurantRequestDTO) {
		UpdateRestaurantResponseDTO updateRestaurantResponseDTO = new UpdateRestaurantResponseDTO();
		Restaurant restaurant = restaurantRepository.findById(updateRestaurantRequestDTO.getId()).get();
		RestaurantIndex restaurantIndex = restaurantElasticRepository.findById(restaurant.getId()).get();
		if (updateRestaurantRequestDTO.getName() != null
				&& !"".equalsIgnoreCase(updateRestaurantRequestDTO.getName())) {
			restaurantIndex.setName(restaurant.getName());
			restaurant.setName(updateRestaurantRequestDTO.getName());
		}
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setId(restaurant.getContactInfo().getId());
		if (updateRestaurantRequestDTO.getEmail() != null
				&& !"".equalsIgnoreCase(updateRestaurantRequestDTO.getEmail())) {
			contactInfo.setEmail(updateRestaurantRequestDTO.getEmail());
		} else {
			contactInfo.setEmail(restaurant.getContactInfo().getEmail());
		}
		if (updateRestaurantRequestDTO.getPhoneNumber() != null
				&& !"".equalsIgnoreCase(updateRestaurantRequestDTO.getPhoneNumber())) {
			contactInfo.setPhoneNumber(updateRestaurantRequestDTO.getPhoneNumber());
		} else {
			contactInfo.setPhoneNumber(restaurant.getContactInfo().getPhoneNumber());
		}
		contactInfo.setRestaurant(restaurant);
		restaurant.setContactInfo(contactInfo);
		restaurantRepository.save(restaurant);
		restaurantElasticRepository.save(restaurantIndex);
		updateRestaurantResponseDTO.setName(restaurant.getName());
		updateRestaurantResponseDTO.setEmail(contactInfo.getEmail());
		updateRestaurantResponseDTO.setPhoneNumber(contactInfo.getPhoneNumber());
		return updateRestaurantResponseDTO;
	}

	// Restaurant ADMIN ONLY
	@Transactional
	public int deleteRestaurant(int id) {
		List<Cart> carts = cartRepository.findAllByRestaurantId(id);
		for (Cart cart : carts) {
			cartItemRepository.deleteByCartId(cart.getId());
		}
		addressRepository.deleteByRestaurantId(id);
		contactInfoRepository.deleteByRestaurantId(id);
		restaurantItemRepository.deleteByRestaurantId(id);
		cartRepository.deleteByRestaurantId(id);

		Restaurant restaurant = restaurantRepository.findById(id).get();
		restaurantRepository.delete(restaurant);
		restaurantElasticRepository.deleteById(id);

		return id;
	}

	// Restaurant OWNER ONLY
	public UpdateRestaurantTimingResponseDTO updateRestaurantTiming(
			UpdateRestaurantTimingRequestDTO updateRestaurantTimingRequestDTO) throws FoodDeliveryException {
		Restaurant restaurant = restaurantRepository.findById(updateRestaurantTimingRequestDTO.getId()).get();
		LocalTime startTime = updateRestaurantTimingRequestDTO.getStartTime();
		LocalTime endTime = updateRestaurantTimingRequestDTO.getEndTime();
		if (startTime == null && endTime == null) {
			throw new FoodDeliveryException("No time provided for update");
		}
		if (startTime != null) {
			restaurant.setStartTime(updateRestaurantTimingRequestDTO.getStartTime());
		} else {
		}
		if (endTime != null) {
			restaurant.setEndTime(updateRestaurantTimingRequestDTO.getEndTime());
		}
		restaurantRepository.save(restaurant);
		UpdateRestaurantTimingResponseDTO updateRestaurantTimingResponseDTO = new UpdateRestaurantTimingResponseDTO();
		updateRestaurantTimingResponseDTO.setStartTime(restaurant.getStartTime());
		updateRestaurantTimingResponseDTO.setEndTime(restaurant.getEndTime());
		return updateRestaurantTimingResponseDTO;
	}

	// Restaurant OWNER ONLY
	public CreateRestaurantItemResponseDTO addItem(CreateRestaurantItemRequestDTO createRestaurantItemRequestDTO)
			throws FoodDeliveryException {
		Optional<Restaurant> restaurantOpt = restaurantRepository
				.findById(createRestaurantItemRequestDTO.getRestaurantId());
		Restaurant restaurant = new Restaurant();
		if (restaurantOpt.isEmpty()) {
			throw new FoodDeliveryException(
					"No restaurant present for the given id :" + createRestaurantItemRequestDTO.getRestaurantId());
		} else {
			restaurant = restaurantOpt.get();
		}
		RestaurantItem restaurantItem = new RestaurantItem();

		restaurantItem.setPrice(createRestaurantItemRequestDTO.getPrice());
		int rating = createRestaurantItemRequestDTO.getRating();
		if (rating < 1 || rating > 5) {
			throw new FoodDeliveryException(
					"Incorrect Rating Provided, Please provide a value between 1-5. Rating provided : "
							+ createRestaurantItemRequestDTO.getRating());
		} else {
			restaurantItem.setRating(rating);
		}
		restaurantItem.setRestaurant(restaurant);

		FoodItem foodItem = new FoodItem();
		foodItem.setCuisine(createRestaurantItemRequestDTO.getCuisine());
		foodItem.setDescription(createRestaurantItemRequestDTO.getDescription());
		foodItem.setIsVeg(createRestaurantItemRequestDTO.getIsVeg());
		foodItem.setName(createRestaurantItemRequestDTO.getName());
		restaurantItem.setFoodItem(foodItem);

		foodItemRepository.save(foodItem);
		restaurantItemRepository.save(restaurantItem);

		Set<RestaurantItem> restaurantItems = new HashSet<>();
		restaurantItems.add(restaurantItem);
		restaurant.setRestaurantItems(restaurantItems);
		restaurantRepository.save(restaurant);

		CreateRestaurantItemResponseDTO createRestaurantItemResponseDTO = new CreateRestaurantItemResponseDTO();
		createRestaurantItemResponseDTO.setItemId(restaurantItem.getId());
		createRestaurantItemResponseDTO.setCuisine(restaurantItem.getFoodItem().getCuisine());
		createRestaurantItemResponseDTO.setDescription(restaurantItem.getFoodItem().getDescription());
		createRestaurantItemResponseDTO.setIsVeg(restaurantItem.getFoodItem().getIsVeg());
		createRestaurantItemResponseDTO.setName(restaurantItem.getFoodItem().getName());
		createRestaurantItemResponseDTO.setPrice(restaurantItem.getPrice());
		createRestaurantItemResponseDTO.setRating(restaurantItem.getRating());
		return createRestaurantItemResponseDTO;
	}

	// Restaurant OWNER ONLY
	public int updateItemAvailability(int itemId, boolean isAvailable) {
		RestaurantItem restaurantItem = restaurantItemRepository.findById(itemId).get();
		restaurantItem.setIsAvailable(isAvailable);

		Restaurant restaurant = restaurantRepository.findById(restaurantItem.getRestaurant().getId()).get();
		Set<RestaurantItem> restaurantItems = restaurant.getRestaurantItems();
		restaurantItems.add(restaurantItem);

		restaurant.setRestaurantItems(restaurantItems);
		restaurantItemRepository.save(restaurantItem);
		restaurantRepository.save(restaurant);
		return restaurantItem.getId();
	}

	// Restaurant OWNER ONLY
	public RestaurantItem updateItemPrice(int itemId, float price) {
		RestaurantItem restaurantItem = restaurantItemRepository.findById(itemId).get();
		restaurantItem.setPrice(price);
		restaurantItemRepository.save(restaurantItem);
		return restaurantItem;
	}

	// Restaurant OWNER ONLY
	public int deleteItem(int restaurantItemId) {
		restaurantItemRepository.deleteById(restaurantItemId);
		return restaurantItemId;
	}

	public OrderStatus approveOrderStatus(int orderId, int restaurantId) throws FoodDeliveryException {
		return markStatusChange(orderId, restaurantId, OrderStatus.RESTAURANT_ACCEPTED);
	}

	public OrderStatus markStatusChange(int orderId, int restaurantId, OrderStatus orderStatus)
			throws FoodDeliveryException {

		Order order = orderRepository.findById(orderId).get();
		if (order != null) {
			int dbRestaurantId = order.getRestaurant().getId();
			OrderStatus dbOrderStatus = order.getOrderStatus();
			if (restaurantId == dbRestaurantId) {
				OrderStatus previousOrderStatus = null;
				switch (orderStatus) {
				case RESTAURANT_ACCEPTED:
					previousOrderStatus = OrderStatus.ORDER_PLACED;
					break;
				case FOOD_PREPARED:
					previousOrderStatus = OrderStatus.RESTAURANT_ACCEPTED;
					break;
				case FOOD_PICKED_UP:
					if (dbOrderStatus == OrderStatus.FOOD_PICKED_UP) {
						throw new FoodDeliveryException(
								"Food is alredy picked up. No more changes to order can be done. Final Order Status as : "
										+ dbOrderStatus);
					} else {
						previousOrderStatus = OrderStatus.FOOD_PREPARED;
					}
					break;
				case ORDER_PLACED:
					throw new FoodDeliveryException("This OrderStatus is not allowed here.");
				default:
					throw new FoodDeliveryException("Incorrect Order Status Sent.");
				}
				if (dbOrderStatus == previousOrderStatus) {
					order.setOrderStatus(orderStatus);
				} else {
					throw new FoodDeliveryException("Wrong Order Status. DataBase OrderStatus : " + dbOrderStatus);
				}
			} else {
				throw new FoodDeliveryException("The order does not belong to this restaurant.");
			}
		}
		orderRepository.save(order);
		return order.getOrderStatus();

	}

}
