package tave.crezipsa.crezipsa.application.trend.port;

import tave.crezipsa.crezipsa.application.trend.model.TrendDetailResult;
import tave.crezipsa.crezipsa.application.trend.model.TrendItem;
import tave.crezipsa.crezipsa.application.trend.model.TrendSearchResult;
import tave.crezipsa.crezipsa.domain.trend.entity.KeywordStoraged;
import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;

import java.util.List;

public interface TrendQueryPort {
	List<TrendItem> findTopKeywordsByPlatformAndCategory(String platform, String category);
	List<TrendItem> findTopKeywordsByCategory(List<String> categories);
	TrendDetailResult findSelectedKeywordDetailByTrendId(long trendId);
	void saveTrend(long userId, TrendCommand trendCommand);
	List<KeywordStoraged> findStoredKeywordsByUserId(long userId);
	TrendSearchResult findKeywordByKeyword(String keyword);

}
