package tave.crezipsa.crezipsa.application.trend.usecase;

import tave.crezipsa.crezipsa.application.trend.dto.response.*;
import tave.crezipsa.crezipsa.application.trend.dto.request.TrendSaveRequest;

import java.util.List;


public interface TrendUsecase {

    List<TrendResponse> getTopTrends(String platform, String category);
	TrendDetailResponse getTrendDetail(long trendId);
	void saveTrend(long userId, TrendSaveRequest request);
	TrendSearchResponse searchTrend(long userId, String keyword);
	List<KeywordResponse> getKeywordStoraged(long userId);
	List<TrendResponse> recommendTrendsByInterests(long userId);
	List<UserHistoryResponse> getUserHistory(Long userId);
	void deleteOneUserHistory(long userId, int historyId);
	void deleteAllUserHistory(long userId);

}
