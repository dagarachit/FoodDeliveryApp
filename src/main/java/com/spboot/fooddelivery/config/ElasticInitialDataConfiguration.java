package com.spboot.fooddelivery.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import com.spboot.fooddelivery.model.Restaurant;
import com.spboot.fooddelivery.model.RestaurantIndex;
import com.spboot.fooddelivery.repository.RestaurantElasticRepository;
import com.spboot.fooddelivery.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Component
public class ElasticInitialDataConfiguration implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired
	RestaurantElasticRepository restaurantElasticRepository;
	@Autowired
	RestaurantRepository restaurantRepository;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//Elastic data delete and setup.
		restaurantElasticRepository.deleteAll();
		List<Restaurant> restaurants =  restaurantRepository.findAll();
		List<RestaurantIndex> restaurantIndexes=new ArrayList<>();
		for(Restaurant restaurant : restaurants) {
			RestaurantIndex restaurantIndex = new RestaurantIndex();
			restaurantIndex.setId(restaurant.getId());
			restaurantIndex.setLocation(new GeoPoint( restaurant.getAddress().getLatitude(),restaurant.getAddress().getLongitude() ) );
			restaurantIndex.setName(restaurant.getName());
			restaurantIndexes.add(restaurantIndex);
		}
		restaurantElasticRepository.saveAll(restaurantIndexes);
		
	}

	
}
