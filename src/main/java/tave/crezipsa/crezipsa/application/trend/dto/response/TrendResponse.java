package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.application.trend.model.TrendItem;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

public record TrendResponse(
        Long id,
        String keyword,
        int rank,
        Platform platform,
        String category,
        String trendDirection
) {
    public static TrendResponse from(TrendItem row) {
        Platform platform = row.platform() == null ? null : Platform.valueOf(row.platform());

        return new TrendResponse(
                row.id(),
                row.keyword(),
                row.rank(),
                platform,
                row.category(),
                row.trendDirection()
        );
    }
}
