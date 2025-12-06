package com.paypal.userms.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.paypal.userms.dao.UserRepo;
import com.paypal.userms.entity.User;
import com.paypal.userms.error.ErrorDetail;
import com.paypal.userms.pojo.CreateWalletRequest;
import com.paypal.userms.pojo.GenericResponse;
import com.paypal.userms.pojo.JwtResponse;
import com.paypal.userms.pojo.LoginRequest;
import com.paypal.userms.pojo.SignupRequest;
import com.paypal.userms.rest.walletms.WalletmsRestUtils;
import com.paypal.userms.util.JWTUtil;
import com.paypal.userms.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private WalletmsRestUtils walletmsRestUtils;

	public GenericResponse signup(SignupRequest signupRequest) {
		try {
			log.debug("signup:: signupRequest: {}", signupRequest);

			Optional<User> userOpt = userRepo.findByEmail(signupRequest.getEmail());

			if (userOpt.isPresent()) {
				return GenericResponse.builder().success(false)
						.errorDetailList(Arrays.asList(ErrorDetail.USER_ALREADY_EXISTS)).build();
			}
 
			User user = User.builder().name(signupRequest.getName()).email(signupRequest.getEmail())
					.password(passwordEncoder.encode(signupRequest.getPassword())).role("ROLE_USER").build();

			userRepo.save(user);
			
			try {
				CreateWalletRequest createWalletRequest = CreateWalletRequest.builder()
						.userId(user.getId())
						.currency("INR")
						.build();
				
				walletmsRestUtils.createWallet(createWalletRequest);
				
			} catch (Exception e) {
				log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
				userRepo.deleteById(user.getId());
				return GenericResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.WALLET_CREATION_FAILED)).build();
			}
			return GenericResponse.builder().success(true).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return GenericResponse.builder().success(false)
					.errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}
	
	public GenericResponse login(LoginRequest loginRequest) {
		try {
			
			Optional<User> userOpt = userRepo.findByEmail(loginRequest.getEmail());
			
			if(!userOpt.isPresent()) {
				return GenericResponse.builder()
						.errorDetailList(Arrays.asList(ErrorDetail.USER_NOT_FOUND))
						.build();
			}
			
			User user = userOpt.get();
			
			if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
				return GenericResponse.builder()
						.errorDetailList(Arrays.asList(ErrorDetail.INVALID_USER_CREDENTIALS))
						.build();
			}
			
			Map<String, Object> claims = new HashMap<>();
			claims.put("role",user.getRole());
			
			String token = jwtUtil.generateToken(claims, user.getEmail());
			
			return GenericResponse.builder()
					.jwtResponse(JwtResponse.builder().token(token).build())
					.build();
			
		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return GenericResponse.builder().success(false)
					.errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}
}
