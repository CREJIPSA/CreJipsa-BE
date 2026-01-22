package tave.crezipsa.crezipsa.global.exception.handler;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	//  도메인/비즈니스 예외
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<GlobalResponseDto<?>> handleCommonException(CommonException e) {
		var code = e.getErrorCode();
		log.warn("CommonException code={} status={} message={}",
			code.getCode(),
			code.getStatus(),
			code.getMessage()
		);

		return ResponseEntity
			.status(code.getStatus())
			.body(GlobalResponseDto.fail(code));
	}

	//  @Valid 검증 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GlobalResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(err -> err.getDefaultMessage())
			.collect(Collectors.joining(", "));
		return ResponseEntity.badRequest().body(GlobalResponseDto.error(errorMessage));
	}

	//  JSON 파싱 오류
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<GlobalResponseDto<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(GlobalResponseDto.error("요청 데이터 형식이 잘못되었습니다."));
	}

	//  헤더 누락
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<GlobalResponseDto<?>> handleMissingHeader(MissingRequestHeaderException ex) {
		return ResponseEntity
			.status(ErrorCode.MISSING_AUTH_HEADER.getStatus())
			.body(GlobalResponseDto.fail(ErrorCode.MISSING_AUTH_HEADER));
	}

	// @PathVariable, @RequestParam 파라미터 검증
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<GlobalResponseDto<Void>> handleConstraintViolation(ConstraintViolationException ex) {

		String errorMessage = ex.getConstraintViolations().stream()
				.findFirst()
				.map(v -> v.getMessage())
				.orElse("요청 값이 올바르지 않습니다.");

		return ResponseEntity.badRequest().body(GlobalResponseDto.error(errorMessage));
	}

	//  그 외 모든 예외
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalResponseDto<?>> handleUnexpected(Exception e) {
		e.printStackTrace();
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(GlobalResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR));
	}

}
