package com.paypal.userms.rest.walletms;

import org.springframework.stereotype.Component;

import com.paypal.userms.pojo.CreateWalletRequest;
import com.paypal.userms.pojo.GenericResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WalletmsRestUtils {
	
	public GenericResponse createWallet(CreateWalletRequest createWalletRequest) {
		return GenericResponse.builder().build();
	}
}
