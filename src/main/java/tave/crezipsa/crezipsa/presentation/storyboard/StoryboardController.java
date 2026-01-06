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
import tave.crezipsa.crezipsa.application.storyboard.dto.request.CreateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardCutRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardTitleRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardCutResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardEditorResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;
import tave.crezipsa.crezipsa.application.storyboard.usecase.StoryboardUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storyboard")
public class StoryboardController {

	private final StoryboardUsecase storyboardUsecase;

	/**
	 * 스토리보드 생성
	 * - 채팅에서 편집 진입: chatMessageId 포함
	 * - 사이드 탭 새 스토리보드: chatMessageId null
	 * 생성 시 기본 컷 1개도 같이 만들어서 내려줌
	 */
	@PostMapping("/create")
	public GlobalResponseDto<StoryboardEditorResponse> create(
		@AuthenticationPrincipal User user,
		@RequestBody CreateStoryboardRequest request
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.create(user.getUserId(), request)
		);
	}

	/**
	 * 스토리보드 편집 화면 조회
	 * - 문서 정보 + 컷 리스트
	 */
	@GetMapping("/{storyboardId}")
	public GlobalResponseDto<StoryboardEditorResponse> get(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.get(user.getUserId(), storyboardId)
		);
	}

	/**
	 * 내 스토리보드 보관함 리스트
	 */
	@GetMapping
	public GlobalResponseDto<List<StoryboardSummaryResponse>> getMyList(
		@AuthenticationPrincipal User user
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.getMyList(user.getUserId())
		);
	}

	/**
	 * 스토리보드 제목 수정
	 */
	@PatchMapping("/{storyboardId}/title")
	public GlobalResponseDto<Void> update(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId,
		@Validated @RequestBody UpdateStoryboardTitleRequest request
	) {
		storyboardUsecase.updateTitle(user.getUserId(), storyboardId, request);
		return GlobalResponseDto.success();
	}

	/**
	 * 컷 추가 (+ 버튼)
	 */
	@PostMapping("/{storyboardId}/cuts")
	public GlobalResponseDto<StoryboardCutResponse> addCut(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.addCut(user.getUserId(), storyboardId)
		);
	}

	/**
	 * 컷 수정 (편집 저장)
	 * cutId만으로 접근 (storyboardId는 cut에 이미 연결되어 있음)
	 */
	@PatchMapping("/cuts/{cutId}")
	public GlobalResponseDto<StoryboardCutResponse> updateCut(
		@AuthenticationPrincipal User user,
		@PathVariable Long cutId,
		@RequestBody UpdateStoryboardCutRequest request
	) {
		return GlobalResponseDto.success(
			storyboardUsecase.updateCut(user.getUserId(), cutId, request)
		);
	}

	/**
	 * 컷 삭제
	 */
	@DeleteMapping("/cuts/{cutId}")
	public GlobalResponseDto<Void> deleteCut(
		@AuthenticationPrincipal User user,
		@PathVariable Long cutId
	) {
		storyboardUsecase.deleteCut(user.getUserId(), cutId);
		return GlobalResponseDto.success();
	}

	@DeleteMapping("/{storyboardId}")
	public GlobalResponseDto<Void> deleteStoryboard(
		@AuthenticationPrincipal User user,
		@PathVariable Long storyboardId
	) {
		storyboardUsecase.deleteStoryboard(user.getUserId(), storyboardId);
		return GlobalResponseDto.success();
	}

}
