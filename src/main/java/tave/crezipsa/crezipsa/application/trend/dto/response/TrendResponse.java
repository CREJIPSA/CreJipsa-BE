package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;

public record TrendResponse(
        Long id,
        String keyword,
        int rank,
        Platform platform,
        String category
) {
    public static TrendResponse from(TrendRow row) {
        Platform platform = row.platform() == null ? null : Platform.valueOf(row.platform());

        return new TrendResponse(
                row.id(),
                row.keyword(),
                row.rank(),
                platform,
                row.category()
        );
    }
}
