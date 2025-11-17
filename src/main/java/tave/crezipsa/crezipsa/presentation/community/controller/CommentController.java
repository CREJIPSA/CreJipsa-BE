package tave.crezipsa.crezipsa.presentation.community.controller;

import java.util.List;

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
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.application.community.usecase.CommentUsecase;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

	private final CommentUsecase commentUsecase;

	//댓글 달기
	@PostMapping("/{communityId}")
	public GlobalResponseDto<CommentResponse> createComment(
		@PathVariable Long communityId,
		@RequestParam Long userId,           // 임시로 userId 받기 + 나중에 UserContext에서 가져오기
		@Valid @RequestBody CommentCreateRequest request
	) {
		CommentResponse response = commentUsecase.createComment(communityId, userId, request);
		return GlobalResponseDto.success(response);
	}

	// 2) 댓글 수정
	@PatchMapping("/{commentId}")
	public GlobalResponseDto<CommentResponse> updateComment(
		@PathVariable Long commentId,
		@RequestParam Long userId,
		@Valid @RequestBody CommentUpdateRequest request
	) {
		CommentResponse response = commentUsecase.updateComment(commentId, userId, request);
		return GlobalResponseDto.success(response);
	}

	// 3) 댓글 삭제
	@DeleteMapping("/{commentId}")
	public GlobalResponseDto<Void> deleteComment(
		@PathVariable Long commentId,
		@RequestParam Long userId
	) {
		commentUsecase.deleteComment(commentId, userId);
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
	public GlobalResponseDto<List<CommentResponse>> getMyComments(@RequestParam Long userId) {
		List<CommentResponse> responses = commentUsecase.getMyComments(userId);
		return GlobalResponseDto.success(responses);
	}



}
