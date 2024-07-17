package com.spboot.fooddelivery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spboot.fooddelivery.jwt.security.JwtAuthenticationEntryPoint;
import com.spboot.fooddelivery.jwt.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class FoodDeliverySecurityConfig {
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(CsrfConfigurer::disable).cors(CorsConfigurer::disable).authorizeHttpRequests(customizer -> customizer
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**","/auth/**").permitAll()
//				.requestMatchers().permitAll()
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(HttpMethod.PUT, "/api/v1/restaurants/").hasAuthority("RESTAURANT.UPDATE")
				.requestMatchers(HttpMethod.POST, "/api/v1/restaurants/").hasAuthority("RESTAURANT.CREATE")
				.requestMatchers(HttpMethod.POST, "/api/v1/restaurants/item").hasAuthority("RESTAURANT.ITEMS.CREATE")
				.requestMatchers(HttpMethod.PATCH, "/api/v1/restaurants/{restaurantId}/order/{orderId}/status/{orderStatus}").hasAuthority("ORDER.STATUS.UPDATE")
				.requestMatchers(HttpMethod.PATCH, "/api/v1/restaurants/{restaurantId}/order/{orderId}/approve").hasAuthority("ORDER.STATUS.UPDATE")
				.requestMatchers(HttpMethod.PATCH, "/api/v1/restaurants/updateTimings").hasAuthority("RESTAURANT.TIMING.UPDATE")
				.requestMatchers(HttpMethod.PATCH, "/api/v1/restaurants/item/{itemId}/price/{price}").hasAuthority("RESTAURANT.ITEMS.UPDATE")
				.requestMatchers(HttpMethod.PATCH, "/api/v1/restaurants/item/{itemId}/available/{isAvailable}").hasAuthority("RESTAURANT.ITEMS.UPDATE")
				.requestMatchers(HttpMethod.DELETE, "/api/v1/restaurants/{id}").hasAuthority("RESTAURANT.DELETE")
				.requestMatchers(HttpMethod.DELETE, "/api/v1/restaurants/item/{restaurantItemId}").hasAuthority("RESTAURANT.ITEMS.DELETE")
				.requestMatchers(HttpMethod.POST, "/api/v1/consumers/order").hasAuthority("ORDER.CREATE")
				.requestMatchers(HttpMethod.POST, "/api/v1/consumers/filter").hasAuthority("RESTAURANT.LIST")///pageNo/{pageNo}/pageSize/{pageSize} removed pagination
				.requestMatchers(HttpMethod.GET, "/api/v1/consumers/{consumerId}/restaurant/browse/pageNo/{pageNo}/pageSize/{pageSize}").hasAuthority("RESTAURANT.LIST")
				.requestMatchers(HttpMethod.GET, "/api/v1/consumers/personalized/{consumerId}/pageNo/{pageNo}/pageSize/{pageSize}").hasAuthority("RESTAURANT.ITEMS.PERSONALIZE")
				.requestMatchers(HttpMethod.GET, "/api/v1/consumers/order/status/{orderId}").hasAuthority("ORDER.STATUS")
				.requestMatchers(HttpMethod.GET, "/api/v1/consumers/order/pastOrder/{consumerId}/pageNo/{pageNo}/pageSize/{pageSize}").hasAuthority("ORDER.LIST"))
				.httpBasic(Customizer.withDefaults())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(registry -> registry.requestMatchers(PathRequest.toH2Console()).permitAll())
				.headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
