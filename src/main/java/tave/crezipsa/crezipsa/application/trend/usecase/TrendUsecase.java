package tave.crezipsa.crezipsa.application.trend.usecase;

import tave.crezipsa.crezipsa.application.trend.dto.response.TrendDetailResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendListResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendSearchResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.request.TrendRequest;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendWithUrls;

import java.util.List;


public interface TrendUsecase {

    List<TrendListResponse> execute(String platform, String category);
    TrendDetailResponse getTrendDetail(long trendId);
    void saveTrend(long userId, TrendRequest request);
    TrendSearchResponse searchTrend(String keyword);

}
