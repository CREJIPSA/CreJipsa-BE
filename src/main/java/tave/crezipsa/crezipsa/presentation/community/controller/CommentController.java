package tave.crezipsa.crezipsa.presentation.community.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommentResponse;
import tave.crezipsa.crezipsa.application.community.usecase.CommentUsecase;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

	private final CommentUsecase commentUsecase;

	//댓글 달기
	@PostMapping("/{communityId}")
	public GlobalResponseDto<CommentResponse> createComment(
		@AuthenticationPrincipal User user,
		@PathVariable Long communityId,
		@Valid @RequestBody CommentCreateRequest request
	) {
		CommentResponse response = commentUsecase.createComment(communityId, user.getUserId(), request);
		return GlobalResponseDto.success(response);
	}

	// 2) 댓글 수정
	@PatchMapping("/{commentId}")
	public GlobalResponseDto<CommentResponse> updateComment(
		@AuthenticationPrincipal User user,
		@PathVariable Long commentId,
		@Valid @RequestBody CommentUpdateRequest request
	) {
		CommentResponse response = commentUsecase.updateComment(commentId, user.getUserId(), request);
		return GlobalResponseDto.success(response);
	}

	// 3) 댓글 삭제
	@DeleteMapping("/{commentId}")
	public GlobalResponseDto<Void> deleteComment(
		@AuthenticationPrincipal User user,
		@PathVariable Long commentId
	) {
		commentUsecase.deleteComment(commentId, user.getUserId());
		return GlobalResponseDto.success();
	}

	// 4) 특정 게시글의 전체 댓글 조회
	@GetMapping("/community/{communityId}")
	public GlobalResponseDto<List<CommentResponse>> getComments(@PathVariable Long communityId) {
		List<CommentResponse> responses = commentUsecase.getComments(communityId);
		return GlobalResponseDto.success(responses);
	}

	// 5) 내가 쓴 댓글 조회(내 댓글함)
	@GetMapping("/my")
	public GlobalResponseDto<List<MyCommentResponse>> getMyComments(
		@AuthenticationPrincipal User user,
		@RequestParam(required = false) CommunityField field,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		List<MyCommentResponse> responses = commentUsecase.getMyComments(user.getUserId(), field, page, size);
		return GlobalResponseDto.success(responses);
	}



}
