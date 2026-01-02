package tave.crezipsa.crezipsa.presentation.community.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommunityResponse;
import tave.crezipsa.crezipsa.application.community.usecase.CommunityUseCase;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@Validated
public class CommunityController {

	private final CommunityUseCase communityUseCase;

	//글 생성
	@PostMapping("/create")
	public GlobalResponseDto<CommunityResponse> createCommunity(
		@AuthenticationPrincipal User user,
		@Valid @RequestBody CommunityCreateRequest request)
	{
		CommunityResponse response = communityUseCase.createCommunity(user.getUserId(), request);
		return GlobalResponseDto.success(response);
	}

	// 글 수정
	@PatchMapping("/{communityId}")
	public GlobalResponseDto<CommunityResponse> updateCommunity(
		@PathVariable Long communityId,
		@AuthenticationPrincipal User user,
		@Valid @RequestBody CommunityUpdateRequest request
	) {
		CommunityResponse response = communityUseCase.updateCommunity(communityId, user.getUserId(), request);
		return GlobalResponseDto.success(response);
	}

	//상세 글 조회
	@GetMapping("/{communityId}")
	public GlobalResponseDto<CommunityDetailResponse> getCommunity(@PathVariable Long communityId) {
		return GlobalResponseDto.success(communityUseCase.getCommunity(communityId));
	}

	//전체 글 조회
	@GetMapping
	public GlobalResponseDto<List<CommunitySummaryResponse>> getAllCommunities() {
		List<CommunitySummaryResponse> communityResponses = communityUseCase.getAllCommunities();
		return GlobalResponseDto.success(communityResponses);
	}

	//카테고리별 글 조회
	@GetMapping("/filter")
	public GlobalResponseDto<List<CommunitySummaryResponse>> getCommunitiesByField(
		@RequestParam CommunityField field) {

		List<CommunitySummaryResponse> responses = communityUseCase.getCommunitiesByField(field);
		return GlobalResponseDto.success(responses);
	}

	//내가 쓴 글 조회
	@GetMapping("/my")
	public GlobalResponseDto<List<MyCommunityResponse>> getCommunitiesByUserId(
		@AuthenticationPrincipal User user,
		@RequestParam(required = false) CommunityField field,
		@RequestParam(defaultValue = "latest") String sort,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size){

		List<MyCommunityResponse> responses = communityUseCase.getMyCommunities(user.getUserId(), field,sort, page, size);
		return GlobalResponseDto.success(responses);
	}

	//글 삭제
	@DeleteMapping("/{communityId}")
	public GlobalResponseDto<Void> deleteCommunity( @AuthenticationPrincipal User user,@PathVariable Long communityId) {
		communityUseCase.deleteCommunity(user.getUserId(), communityId);
		return GlobalResponseDto.success();
	}

}
