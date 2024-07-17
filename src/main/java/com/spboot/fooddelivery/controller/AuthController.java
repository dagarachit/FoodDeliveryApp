package com.spboot.fooddelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spboot.fooddelivery.auth.AuthService;
import com.spboot.fooddelivery.dto.JwtAuthResponse;
import com.spboot.fooddelivery.dto.LoginDto;
import com.spboot.fooddelivery.dto.RegisterDto;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
		String token = authService.login(loginDto);

		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setAccessToken(token);

		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody RegisterDto registerDto) {
		authService.register(registerDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
