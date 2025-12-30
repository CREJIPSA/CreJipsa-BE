package tave.crezipsa.crezipsa.presentation.trend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.application.trend.usecase.TrendUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class TrendController {

    private final TrendUsecase trendUsecase;

    @GetMapping
    public GlobalResponseDto<List<TrendResponse>> getTrendList(@AuthenticationPrincipal User user,  @RequestParam String platform){

        return GlobalResponseDto.success(trendUsecase.execute(platform));
    }
}
