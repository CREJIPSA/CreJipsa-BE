package tave.crezipsa.crezipsa.application.trend.port;

import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailRow;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailWithUrls;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendWithUrls;

import java.util.List;

public interface TrendQueryPort {
    List<TrendRow> findTopKeywordsByPlatformAndCategory(String platform, String category);
    List<TrendRow> findTopKeywordsByCategory(List<String> categories);
    TrendDetailWithUrls findSelectedKeywordDetailBytrendId(long trendId);
    void saveTrend(long userId, TrendCommand trendCommand);
    TrendWithUrls findKeywordByKeyword(String keyword);

}
