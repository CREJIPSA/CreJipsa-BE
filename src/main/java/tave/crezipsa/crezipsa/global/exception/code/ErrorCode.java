package tave.crezipsa.crezipsa.global.exception.code;

public enum ErrorCode implements BaseErrorCode {
	// 유저 관련
	USER_NOT_FOUND(404, "U40401", "사용자를 찾을 수 없습니다."),

	// 인증 관련
	MISSING_AUTH_HEADER(401, "A40101", "Authorization 헤더가 누락되었습니다."),
	INVALID_TOKEN(401, "A40102", "토큰이 유효하지 않습니다."),

	// 서버 오류
	INTERNAL_SERVER_ERROR(500, "S50001", "서버 내부 오류가 발생했습니다.");

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
}

