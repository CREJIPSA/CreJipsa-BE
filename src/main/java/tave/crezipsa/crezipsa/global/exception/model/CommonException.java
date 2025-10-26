package tave.crezipsa.crezipsa.global.exception.model;

import tave.crezipsa.crezipsa.global.exception.code.BaseErrorCode;

public class CommonException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public CommonException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BaseErrorCode getErrorCode() {
		return errorCode;
	}
}

