package tave.crezipsa.crezipsa.application.trend.usecase;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendDetailResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendListResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendSearchResponse;
import tave.crezipsa.crezipsa.application.trend.dto.response.request.TrendRequest;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailWithUrls;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendWithUrls;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TrendUsecaseImpl implements TrendUsecase {

    private final TrendQueryPort trendQueryPort;

    @Override
    public List<TrendListResponse> getTopTrends(String platform, String category) {

        List<TrendRow> trendRowList = trendQueryPort.findTopKeywordsByPlatformAndCategory(platform, category);
        return trendRowList.stream()
                .map(TrendListResponse::from)
                .toList();
    }

    @Override
    public TrendDetailResponse getTrendDetail(long trendId) {

        TrendDetailWithUrls trendDetailWithUrls = trendQueryPort.findSelectedKeywordDetailBytrendId(trendId);
        return TrendDetailResponse.from(trendDetailWithUrls);
    }

    @Override
    public void saveTrend(long userId, TrendRequest request) {
        trendQueryPort.saveTrend(userId, request.from(request));
    }

    @Override
    public TrendSearchResponse searchTrend(String keyword) {
        return TrendSearchResponse.from(trendQueryPort.findKeywordByKeyword(keyword));
    }

}
