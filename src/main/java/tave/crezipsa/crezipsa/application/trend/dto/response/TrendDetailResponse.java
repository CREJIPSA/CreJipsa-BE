package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailRow;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailWithUrls;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendUrlRow;

import java.util.List;

public record TrendDetailResponse(
        Long id,
        Platform platform,
        int overall_rank,
        int category_rank,
        String keyword,
        int frequency,
        List<TrendUrlResponse> urls

) {
    public static TrendDetailResponse from(TrendDetailWithUrls trendDetailWithUrls) {

        List<TrendUrlResponse> urls = List.of();

        if (trendDetailWithUrls.urls() != null) {
            urls = trendDetailWithUrls.urls().stream()
                    .map(TrendUrlResponse::from)
                    .toList();
        }

        return new TrendDetailResponse(
                trendDetailWithUrls.detailRow().id(),
                Platform.valueOf(trendDetailWithUrls.detailRow().platform()),
                trendDetailWithUrls.detailRow().overall_rank(),
                trendDetailWithUrls.detailRow().category_rank(),
                trendDetailWithUrls.detailRow().keyword(),
                trendDetailWithUrls.detailRow().frequency(),
                urls
        );
    }
}
