package tave.crezipsa.crezipsa.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import tave.crezipsa.crezipsa.global.exception.code.BaseErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON 응답에서 제외
public class GlobalResponseDto<T> {

	private final boolean success;
	private final int status;
	private final String message;
	private final String errorCode;
	private final T result;

	// private 생성자 — 정적 팩토리 메서드로만 생성 가능
	private GlobalResponseDto(boolean success, int status, String message, String errorCode, T result) {
		this.success = success;
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
		this.result = result;
	}

	/**  성공 응답 (데이터 포함) */
	public static <T> GlobalResponseDto<T> success(T result) {
		return new GlobalResponseDto<>(true, 200, "요청이 성공적으로 처리되었습니다.", null, result);
	}

	/**  성공 응답 (데이터 없음) */
	public static <T> GlobalResponseDto<T> success() {
		return new GlobalResponseDto<>(true, 200, "요청이 성공적으로 처리되었습니다.", null, null);
	}

	/**  실패 응답 (에러 코드 기반) */
	public static <T> GlobalResponseDto<T> fail(BaseErrorCode errorCode) {
		return new GlobalResponseDto<>(
			false,
			errorCode.getStatus(),
			errorCode.getMessage(),
			errorCode.getCode(),
			null
		);
	}

	/**  단순 실패 응답 (커스텀 메시지) */
	public static <T> GlobalResponseDto<T> error(String message) {
		return new GlobalResponseDto<>(false, 400, message, null, null);
	}

	// ===== Getter ===== //
	public boolean isSuccess() { return success; }
	public int getStatus() { return status; }
	public String getMessage() { return message; }
	public String getErrorCode() { return errorCode; }
	public T getResult() { return result; }
}
