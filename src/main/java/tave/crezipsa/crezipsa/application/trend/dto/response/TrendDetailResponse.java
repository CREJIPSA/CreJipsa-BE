package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.application.trend.model.TrendDetail;
import tave.crezipsa.crezipsa.application.trend.model.TrendDetailResult;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.util.List;

public record TrendDetailResponse(
        Long id,
        Platform platform,
        String category,
        int overall_rank,
        int category_rank,
        String keyword,
        double viralityScore,
        List<TrendUrlResponse> urls

) {
    public static TrendDetailResponse from(TrendDetailResult trendDetailWithUrls) {
        TrendDetail detail = trendDetailWithUrls.detail();
        List<TrendUrlResponse> urls = trendDetailWithUrls.urls() == null
                ? List.of()
                : trendDetailWithUrls.urls().stream().map(TrendUrlResponse::from).toList();

        return new TrendDetailResponse(
                detail.id(),
                Platform.valueOf(detail.platform()),
                detail.category(),
                detail.overallRank(),
                detail.categoryRank(),
                detail.keyword(),
                detail.viralityScore(),
                urls
        );
    }
}
