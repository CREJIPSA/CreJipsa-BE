package tave.crezipsa.crezipsa.presentation.trend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendDetailResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendSearchResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.request.TrendRequest;
import tave.crezipsa.crezipsa.application.trend.usecase.TrendUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
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
    public GlobalResponseDto saveTrend(@AuthenticationPrincipal User user, @RequestBody TrendRequest trendRequest){
        trendUsecase.saveTrend(user.getUserId(), trendRequest);
        return GlobalResponseDto.success();
    }

    @GetMapping("/search/{trend}")
    public GlobalResponseDto<TrendSearchResponse> searchTrend(@AuthenticationPrincipal User user, @PathVariable String trend){
        return GlobalResponseDto.success(trendUsecase.searchTrend(trend));
    }

    @GetMapping("recommendations/by-platform")
    public GlobalResponseDto<List<TrendResponse>> recommendationsByPlatform(@AuthenticationPrincipal User user){
        return GlobalResponseDto.success(trendUsecase.getTopTrends(user.getMainPlatform().toString(), null));
    }

    @GetMapping("recommendations/by-interests")
    public GlobalResponseDto<List<TrendResponse>> recommendationsByInterests(@AuthenticationPrincipal User user){
        return GlobalResponseDto.success(trendUsecase.recommendTrendsByInterests(user.getUserId()));
    }
}
