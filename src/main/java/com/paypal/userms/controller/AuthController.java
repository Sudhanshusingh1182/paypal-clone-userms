package com.paypal.userms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.userms.pojo.GenericResponse;
import com.paypal.userms.pojo.LoginRequest;
import com.paypal.userms.pojo.SignupRequest;
import com.paypal.userms.service.AuthServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthController {

	@Autowired
	private AuthServiceImpl authServiceImpl;

	@PostMapping("/api/auth/v1/signup")
	public GenericResponse signup(@RequestBody SignupRequest signupRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return authServiceImpl.signup(signupRequest);
		} finally {
			log.debug("Time taken to signup: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@PostMapping("/api/auth/v1/login")
	public GenericResponse login(@RequestBody LoginRequest loginRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return authServiceImpl.login(loginRequest);
		} finally {
			log.debug("Time taken to login: {}", (System.currentTimeMillis() - currentTime));
		}
	}

}
