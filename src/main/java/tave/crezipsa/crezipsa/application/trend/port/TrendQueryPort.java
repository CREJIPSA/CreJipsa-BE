package tave.crezipsa.crezipsa.application.trend.port;

import tave.crezipsa.crezipsa.domain.trend.entity.KeywordStoraged;
import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailWithUrls;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendWithUrls;

import java.util.List;

public interface TrendQueryPort {
	List<TrendRow> findTopKeywordsByPlatformAndCategory(String platform, String category);
	List<TrendRow> findTopKeywordsByCategory(List<String> categories);
	TrendDetailWithUrls findSelectedKeywordDetailByTrendId(long trendId);
	void saveTrend(long userId, TrendCommand trendCommand);
	List<KeywordStoraged> findStoredKeywordsByUserId(long userId);
	TrendWithUrls findKeywordByKeyword(String keyword);

}
