package tave.crezipsa.crezipsa.global.exception.code;

public enum ErrorCode implements BaseErrorCode {
	// 유저 관련
	USER_NOT_FOUND(404, "U40401", "사용자를 찾을 수 없습니다."),
	USER_ALREADY_EXISTS_EMAIL(404, "U40402", "이미 가입된 이메일입니다."),
	USER_ALREADY_EXISTS_NICKNAME(404, "U40403", "이미 존재하는 닉네임입니다. "),
	USER_INVALID_ROLE(401, "U40101","권한이 없는 사용자입니다."),
	USER_INVALID_ID(401,"U40102", "존재하지 않는 유저입니다."),

	//카테고리 관련
	INVALD_INTEREST(404,"I40401", "존재하지 않은 사용자 관심 카테고리입니다."),
	ALREADY_INTEREST(400,"I40001","이미 선택한 사용자 관심 카테고리입니다."),

	// 인증 관련
	MISSING_AUTH_HEADER(400, "A40101", "Authorization 헤더가 누락되었습니다."),
	INVALID_TOKEN(401, "A40102", "토큰이 유효하지 않습니다."),
	INVALID_REFRESH_TOKEN(401, "A40103", "토큰이 일치하지 않습니다."),
	ACCESS_TOKEN_EXPIRED(401,"A40104","토큰 유효시간이 만료되었습니다."),
	KAKAO_USERINFO_FAILED(400, "A40105", "카카오 사용자 정보를 가져오지 못했습니다."),

	// 스토리보드 관련
	INVALID_SENDER_TYPE(401,"S40101", "Sender TYPE이 올바르지 않습니다."),
	STORYBOARD_NOT_FOUND(404, "S40401", "존재하지 않는 스토리보드입니다."),

	//제미나이 관련
	GEMINI_EMPTY_RESPONSE(404,"G40401", "제미나이 응답 오류입니다."),
	GEMINI_CLIENT_ERROR(400, "G40001", "Gemini 요청 값이 올바르지 않습니다."),
	GEMINI_SERVER_ERROR(502, "G50202", "Gemini 서버 오류가 발생했습니다."),
	GEMINI_REQUEST_FAILED(504, "G50401", "Gemini 요청 처리 중 오류가 발생했습니다."),

	// 커뮤니티 관련
	COMMUNITY_NOT_FOUND(404, "C40401", "해당 커뮤니티 게시글을 찾을 수 없습니다."),
	INVALID_FIELD_TYPE(400, "C40001", "유효하지 않은 게시글 유형입니다."),
	INVALID_COMMENT_CONTENT(400, "C4003", "댓글 내용은 비어 있을 수 없습니다."),
	INVALID_COMMENT_DEPTH(400, "C4004", "대댓글엔 대댓글을 달 수 없습니다."),
	INVALID_PARENT_COMMUNITY(400, "C4005", "대댓글은 동일한 게시글 내에서만 달 수 있습니다"),
	INVALID_TIP_UPLOAD(400, "C4006", "팁 게시물은 최소 이미지 1개 첨부가 필수입니다"),
	COMMENT_NOT_FOUND(404, "C40402", "해당 댓글을 찾을 수 없습니다."),
	ALREADY_LIKED(400, "C40002", "이미 좋아요를 누른 게시글입니다."),
	NOT_LIKED(400,"C40003", "좋아요를 누르지 않은 글입니다."),
	UNAUTHORIZED_COMMUNITY(404, "C40403", "게시글 권한이 없습니다"),
	UNAUTHORIZED_COMMENT(404, "C40404", "댓글 권한이 없습니다"),

	//스토리보드 관련
	CHAT_NOT_FOUND(404, "C40405", "대화내역이 존재하지 않습니다"),


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

