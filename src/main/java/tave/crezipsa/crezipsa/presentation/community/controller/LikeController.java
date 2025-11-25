package tave.crezipsa.crezipsa.presentation.community.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.response.MyLikedCommunityResponse;
import tave.crezipsa.crezipsa.application.community.usecase.LikeUseCase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/likes")
public class LikeController {

	private final LikeUseCase likeUseCase;
/*
	@PostMapping("/{communityId}")
	public GlobalResponseDto<Void> like(@AuthenticationPrincipal User user, @PathVariable Long communityId) {
		likeUseCase.like(user.getUserId(), communityId);
		return GlobalResponseDto.success();
	}

	@DeleteMapping("/{communityId}")
	public GlobalResponseDto<Void> unlike(@AuthenticationPrincipal User user, @PathVariable Long communityId) {
		likeUseCase.unlike(user.getUserId(), communityId);
		return GlobalResponseDto.success();
	}

	@GetMapping("/{communityId}")
	public GlobalResponseDto<Long> getLikeCount(@PathVariable Long communityId) {
		return GlobalResponseDto.success(likeUseCase.getLikeCount(communityId));
	}

	@GetMapping("/me")
	public GlobalResponseDto<List<MyLikedCommunityResponse>> getMyLikedCommunities(
		@AuthenticationPrincipal User user,
		Pageable pageable
	) {
		var slice = likeUseCase.getMyLikedCommunities(user.getUserId(), pageable);
		return GlobalResponseDto.success(slice.getContent());
	}
*/
    @PostMapping("/{communityId}")
	public GlobalResponseDto<Void> like(@RequestParam Long userId, @PathVariable Long communityId) {
		likeUseCase.like(userId, communityId);
		return GlobalResponseDto.success();
	}

	@DeleteMapping("/{communityId}")
	public GlobalResponseDto<Void> unlike(@RequestParam Long userId, @PathVariable Long communityId) {
		likeUseCase.unlike(userId, communityId);
		return GlobalResponseDto.success();
	}

	@GetMapping("/{communityId}")
	public GlobalResponseDto<Long> getLikeCount(@PathVariable Long communityId) {
		return GlobalResponseDto.success(likeUseCase.getLikeCount(communityId));
	}

	@GetMapping("/me")
	public GlobalResponseDto<List<MyLikedCommunityResponse>> getMyLikedCommunities(
		@RequestParam Long userId,
		Pageable pageable
	) {
		var slice = likeUseCase.getMyLikedCommunities(userId, pageable);
		return GlobalResponseDto.success(slice.getContent());
	}

}
