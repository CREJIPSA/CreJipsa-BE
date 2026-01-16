package tave.crezipsa.crezipsa.application.trend.usecase;

import tave.crezipsa.crezipsa.application.trend.dto.response.TrendDetailResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendSearchResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.request.TrendRequest;
import tave.crezipsa.crezipsa.domain.user.entity.User;

import java.util.List;


public interface TrendUsecase {

    List<TrendResponse> getTopTrends(String platform, String category);
    TrendDetailResponse getTrendDetail(long trendId);
    void saveTrend(long userId, TrendRequest request);
    TrendSearchResponse searchTrend(String keyword);
    List<TrendResponse> recommendTrendsByInterests(long userId);

}
