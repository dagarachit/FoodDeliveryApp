package com.spboot.fooddelivery.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.spboot.fooddelivery.model.RestaurantIndex;

@Repository
public interface RestaurantElasticRepository extends ElasticsearchRepository<RestaurantIndex, Integer> {
	
//	List<RestaurantIndex> findByLocationNear(GeoPoint location, String distance);

	List<RestaurantIndex> findByLocationNear(GeoPoint geoPoint, String string, PageRequest pageRequest);

}
