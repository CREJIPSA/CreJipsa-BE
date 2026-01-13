package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;

public record TrendListResponse(

        Long id,
        String keyword,
        int rank,
        Platform platform,
        String category
) {
    public static TrendListResponse from(TrendRow row) {
        Platform platform = row.platform() == null ? null : Platform.valueOf(row.platform());

        return new TrendListResponse(
                row.id(),
                row.keyword(),
                row.rank(),
                platform,
                row.category()
        );
    }
}
