package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.community.dto.request.CommentCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommentResponse;

public interface CommentUsecase {

	CommentResponse createComment(Long communityId, Long userId, CommentCreateRequest request);
	CommentResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request);
	void deleteComment(Long commentId, Long userId);
	List<CommentResponse> getComments(Long communityId);
	List<MyCommentResponse> getMyComments(Long userId, String sort, int page, int size);


}
