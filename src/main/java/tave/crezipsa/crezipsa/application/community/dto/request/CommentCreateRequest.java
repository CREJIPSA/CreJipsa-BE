package tave.crezipsa.crezipsa.application.community.dto.request;

public record CommentCreateRequest(
	Long parentId,
	String content
) {
}
