package com.paypal.userms.error;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorDetail {

	// 1xxx SYSTEM errors
	INTERNAL_SERVER_ERROR("USERMS_1001", "internal.server.error", ErrorType.SYSTEM,
			"An internal server error occurred. Please try after sometime."),
	USER_NOT_AUTHENTICATED("USERMS_1002", "user.not.authenticated", ErrorType.SYSTEM, "User not authenticated."),
	NOT_ALLOWED_TO_USE_APP("USERMS_1003", "not.allowed.to.use.app", ErrorType.SYSTEM,
			"You are not allowed to use this app."),
	SOFT_UPGRADE_AVAILABLE("USERMS_1004", "soft.upgrade.available", ErrorType.SYSTEM, "Soft upgrade available."),
	HARD_UPGRADE_REQUIRED("USERMS_1005", "hard.upgrade.required", ErrorType.SYSTEM, "Hard upgrade required."),

	// 2xxx VALIDATION errors
	USER_NOT_FOUND("USERMS_2001", "user.not.found", ErrorType.VALIDATION , "User not found."),
	USER_ALREADY_EXISTS("USERMS_2002", "user.already.exists", ErrorType.VALIDATION , "User already exists."),
	INVALID_USER_CREDENTIALS("USERMS_2003", "invalid.user.credentials", ErrorType.VALIDATION , "Invalid user credentials."),
	WALLET_CREATION_FAILED("USERMS_2004","wallet.creation.failed", ErrorType.BUSINESS, "Wallet creation failed.");
	
	private static final String TO_STRING_TEMPLATE = "code: %s, propertyKey: %s, errorType: %s, defaultMessage: %s";

	String code;
	String propertyKey;
	ErrorType errorType;
	String defaultMessage;

	private ErrorDetail(String code, String propertyKey, ErrorType errorType, String defaultMessage) {
		this.code = code;
		this.propertyKey = propertyKey;
		this.errorType = errorType;
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_TEMPLATE, code, propertyKey, errorType, defaultMessage);
	}

	public enum ErrorType {
		SYSTEM("system"), VALIDATION("validation"), BUSINESS("business");

		String error;

		private ErrorType(String error) {
			this.error = error;
		}

		@Override
		public String toString() {
			return error;
		}
	}

	public String getCode() {
		return code;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}