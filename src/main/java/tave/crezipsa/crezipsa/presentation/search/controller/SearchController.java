package tave.crezipsa.crezipsa.presentation.search.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.search.dto.response.UnifiedSearchResponse;
import tave.crezipsa.crezipsa.application.search.usecase.SearchUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchUsecase searchUsecase;

	@GetMapping
	public GlobalResponseDto<UnifiedSearchResponse> search(
		@AuthenticationPrincipal User user,
		@RequestParam String keyword
	) {
		return GlobalResponseDto.success(
			searchUsecase.search(user.getUserId(), keyword)
		);
	}
}
