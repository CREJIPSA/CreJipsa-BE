package tave.crezipsa.crezipsa.presentation.storyboard;

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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.SaveStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardStructuredResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;
import tave.crezipsa.crezipsa.application.storyboard.usecase.StoryboardUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storyboard")
public class StoryboardController {

	private final StoryboardUsecase storyboardUsecase;

	@PostMapping("/create")
	public GlobalResponseDto<StoryboardStructuredResponse> create(
		@AuthenticationPrincipal User user,
		@Validated @RequestBody SaveStoryboardRequest request
	) {
		StoryboardStructuredResponse response =
			storyboardUsecase.createFromChatMessage(
				user.getUserId(),
				request.chatMessageId(),
				request.title()
			);

		return GlobalResponseDto.success(response);
	}

	@GetMapping("/{storyboardId}")
	public GlobalResponseDto<StoryboardStructuredResponse> get(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.get(user.getUserId(), storyboardId)
		);
	}

	@GetMapping
	public GlobalResponseDto<List<StoryboardSummaryResponse>> getMyList(
		@AuthenticationPrincipal User user
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.getMyList(user.getUserId())
		);
	}

	@PatchMapping("/{storyboardId}")
	public GlobalResponseDto<StoryboardStructuredResponse> update(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId,
		@Validated @RequestBody UpdateStoryboardRequest request
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.update(user.getUserId(), storyboardId, request)
		);
	}

	@DeleteMapping("/{storyboardId}")
	public GlobalResponseDto<Void> delete(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId
	) {
		storyboardUsecase.delete(user.getUserId(), storyboardId);
		return GlobalResponseDto.success();
	}

}
