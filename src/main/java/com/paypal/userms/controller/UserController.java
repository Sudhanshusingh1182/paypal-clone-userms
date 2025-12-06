package com.paypal.userms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.userms.pojo.CreateUserRequest;
import com.paypal.userms.pojo.GenericResponse;
import com.paypal.userms.pojo.UserListResponse;
import com.paypal.userms.pojo.UserResponse;
import com.paypal.userms.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@PostMapping("/api/userms/v1/user")
	public GenericResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return userServiceImpl.createUser(createUserRequest);
		} finally {
			log.debug("Time taken createUser: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@GetMapping("/api/userms/v1/userbyid")
	public UserResponse getUserById(@RequestParam(name = "userId", required = true) Long userId) {
		long currentTime = System.currentTimeMillis();
		try {
			return userServiceImpl.getUserById(userId);
		} finally {
			log.debug("Time taken getUserById: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@GetMapping("/api/userms/v1/getallusers")
	public UserListResponse getAllUsers() {
		long currentTime = System.currentTimeMillis();
		try {
			return userServiceImpl.getAllUsers();
		} finally {
			log.debug("Time taken getAllUsers: {}", (System.currentTimeMillis() - currentTime));
		}
	}

}
