package tave.crezipsa.crezipsa.global.exception.model;

import tave.crezipsa.crezipsa.global.exception.code.BaseErrorCode;

public class ErrorResponse {

	private final int status;
	private final String code;
	private final String message;

	public ErrorResponse(BaseErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public static ErrorResponse of(BaseErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}

	public int getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
