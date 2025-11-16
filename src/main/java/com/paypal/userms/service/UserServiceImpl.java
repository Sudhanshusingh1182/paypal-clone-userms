package com.paypal.userms.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.userms.controller.UserController;
import com.paypal.userms.dao.UserRepo;
import com.paypal.userms.entity.User;
import com.paypal.userms.error.ErrorDetail;
import com.paypal.userms.pojo.CreateUserRequest;
import com.paypal.userms.pojo.GenericResponse;
import com.paypal.userms.pojo.UserAPI;
import com.paypal.userms.pojo.UserListResponse;
import com.paypal.userms.pojo.UserResponse;
import com.paypal.userms.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl {

	@Autowired
	private UserRepo userRepo;

	public GenericResponse createUser(CreateUserRequest createUserRequest) {
		try {
			log.debug("createUser:: createUserRequest: {}", createUserRequest);
			User user = User.builder().name(createUserRequest.getName()).email(createUserRequest.getEmail())
					.password(createUserRequest.getPassword()).build();

			userRepo.save(user);
			return GenericResponse.builder().success(true).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return GenericResponse.builder().success(false)
					.errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}

	}

	public UserResponse getUserById(Long userId) {
		try {
			log.debug("getUserById:: userId: {}", userId);

			Optional<User> userOpt = userRepo.findById(userId);

			if (!userOpt.isPresent()) {
				return UserResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.USER_NOT_FOUND)).build();
			}

			User user = userOpt.get();

			UserAPI userAPI = UserAPI.builder().name(user.getName()).email(user.getEmail()).build();

			return UserResponse.builder().userAPI(userAPI).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return UserResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}

	public UserListResponse getAllUsers() {
		try {
			log.debug("getAllUsers:: Inside the service layer");

			List<User> userList = userRepo.findAll();

			List<UserAPI> userAPIList = userList.stream()
					.map(user -> UserAPI.builder().name(user.getName()).email(user.getEmail()).build()).toList();

			return UserListResponse.builder().userAPIList(userAPIList).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return UserListResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}
}
