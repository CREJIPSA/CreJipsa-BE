package tave.crezipsa.crezipsa.presentation.trend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.application.trend.dto.request.TrendSaveRequest;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendDetailResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.KeywordResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendSearchResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.UserHistoryResponse;
import tave.crezipsa.crezipsa.application.trend.dto.request.TrendSearchRequest;
import tave.crezipsa.crezipsa.application.trend.usecase.TrendUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/main/trend")
public class TrendController {

    private final TrendUsecase trendUsecase;

    @GetMapping
    public GlobalResponseDto<List<TrendResponse>> getTrendList(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "YOUTUBE") String platform, @RequestParam(required = false) String category){
        return GlobalResponseDto.success(trendUsecase.getTopTrends(platform, category));
    }

    @GetMapping("/detail/{trendId}")
    public GlobalResponseDto<TrendDetailResponse> getTrendDetail(@AuthenticationPrincipal User user,@PathVariable long trendId){
        return GlobalResponseDto.success(trendUsecase.getTrendDetail(trendId));
    }

	@PostMapping("/save")
	public GlobalResponseDto<Void> saveTrend(@AuthenticationPrincipal User user, @RequestBody @Valid TrendSaveRequest trendRequest){
		trendUsecase.saveTrend(user.getUserId(), trendRequest);
		return GlobalResponseDto.success();
	}

	@GetMapping("/getUserTrend")
	public GlobalResponseDto<List<KeywordResponse>> getUserTrend(@AuthenticationPrincipal User user){
		return GlobalResponseDto.success(trendUsecase.getKeywordStoraged(user.getUserId()));
	}

	@GetMapping("/search")
	public GlobalResponseDto<TrendSearchResponse> searchTrend(@AuthenticationPrincipal User user, @ModelAttribute @Valid TrendSearchRequest trendSearchRequest) {
		return GlobalResponseDto.success(trendUsecase.searchTrend(user.getUserId(), trendSearchRequest.getTrend()));
	}

	@GetMapping("/recommendations/by-platform")
	public GlobalResponseDto<List<TrendResponse>> recommendationsByPlatform(@AuthenticationPrincipal User user){
		String platform = user.getMainPlatform() == null ? null : user.getMainPlatform().toString();
		return GlobalResponseDto.success(trendUsecase.getTopTrends(platform, null));
	}

	@GetMapping("/recommendations/by-interests")
	public GlobalResponseDto<List<TrendResponse>> recommendationsByInterests(@AuthenticationPrincipal User user){
		return GlobalResponseDto.success(trendUsecase.recommendTrendsByInterests(user.getUserId()));
	}
	@GetMapping("/search-history")
	public GlobalResponseDto<List<UserHistoryResponse>> getUserHistory(@AuthenticationPrincipal User user){
		return GlobalResponseDto.success(trendUsecase.getUserHistory(user.getUserId()));
	}

	@DeleteMapping("/{historyId}")
	public GlobalResponseDto<Void> deleteOneUserHistory(@AuthenticationPrincipal User user, @PathVariable int historyId){
		trendUsecase.deleteOneUserHistory(user.getUserId(), historyId);
		return GlobalResponseDto.success();
	}

	@DeleteMapping("/all-history")
	public GlobalResponseDto<Void> deleteAllUserHistory(@AuthenticationPrincipal User user){
		trendUsecase.deleteAllUserHistory(user.getUserId());
		return GlobalResponseDto.success();
	}
}
