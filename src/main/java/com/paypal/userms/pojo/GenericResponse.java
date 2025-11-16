package com.paypal.userms.pojo;

import java.util.List;

import com.paypal.userms.error.ErrorDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
	private Boolean success;
	private JwtResponse jwtResponse;
	private List<ErrorDetail> errorDetailList;
}
