package tave.crezipsa.crezipsa.global.exception.code;

public enum ErrorCode implements BaseErrorCode {
	// 유저 관련
	USER_NOT_FOUND(404, "U40401", "사용자를 찾을 수 없습니다."),
	USER_ALREADY_EXISTS_EMAIL(404, "U40402", "이미 가입된 이메일입니다."),
	USER_ALREADY_EXISTS_NICKNAME(404, "U40403", "이미 존재하는 닉네임입니다. "),
	// 인증 관련
	MISSING_AUTH_HEADER(401, "A40101", "Authorization 헤더가 누락되었습니다."),
	INVALID_TOKEN(401, "A40102", "토큰이 유효하지 않습니다."),
	KAKAO_USERINFO_FAILED(401, "A40103", "카카오 사용자 정보를 가져오지 못했습니다."),

	// 커뮤니티 관련
	COMMUNITY_NOT_FOUND(404, "C40401", "해당 커뮤니티 게시글을 찾을 수 없습니다."),
	INVALID_FIELD_TYPE(400, "C40001", "유효하지 않은 게시글 유형입니다."),
	INVALID_COMMENT_CONTENT(400, "C4003", "댓글 내용은 비어 있을 수 없습니다."),
	INVALID_COMMENT_DEPTH(400, "C4004", "대댓글엔 대댓글을 달 수 없습니다."),
	INVALID_PARENT_COMMUNITY(400, "C4005", "대댓글은 동일한 게시글 내에서만 달 수 있습니다"),
	COMMENT_NOT_FOUND(404, "C40402", "해당 댓글을 찾을 수 없습니다."),
	ALREADY_LIKED(400, "C40002", "이미 좋아요를 누른 게시글입니다."),
	UNAUTHORIZED_COMMUNITY(404, "C40403", "게시글 권한이 없습니다"),
	UNAUTHORIZED_COMMENT(404, "C40404", "댓글 권한이 없습니다"),

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

