package com.spboot.fooddelivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spboot.fooddelivery.model.RestaurantItem;

@Repository
public interface RestaurantItemRepository extends JpaRepository<RestaurantItem, Integer> {

//	@Query(value = "SELECT ri FROM RestaurantItem ri JOIN FoodItem fi ON fi.ID = ri.foodItem.id WHERE ri.restaurant.id=:restaurantId AND fi.cuisine = :cuisine AND fi.isVeg =:isVeg AND ri.rating>:rating AND fi.name = :name")
//	List<RestaurantItem> findFilteredRestaurantItems(@Param("restaurantId") int restaurantId,
//			@Param("cuisine") Cuisine cuisine, @Param("isVeg") boolean isVeg,
//			@Param("rating") int rating, @Param("name") String name,PageRequest pageRequest);
//
//	
	List<RestaurantItem> findAllByRestaurantIdOrderByRatingDesc(int restaurantId);//, PageRequest pageRequest


//	@Query(nativeQuery = true , value = "SELECT * FROM RESTAURANT_ITEM RI JOIN FOOD_ITEM FI ON FI.ID = RI.FOOD_ITEM_ID "
//			+ "WHERE RI.RESTAURANT_ID=:restaurantId AND FI.CUISINE like '%:cuisine%' AND FI.IS_VEG like '%:isVeg%' AND RI.RATING>:rating")
//	List<Object> findFilteredRestaurantItemsByNativeQuery(@Param("restaurantId")  int restaurantId,
//			@Param("cuisine")  Cuisine cuisine, @Param("isVeg")  Boolean isVeg,
//			@Param("rating") int rating, PageRequest pageRequest);

	
	void deleteByRestaurantId(int id);

	List<RestaurantItem> findByFoodItem_Id(int id);

}
