package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;

public record TrendResponse(

        Long id,
        String keyword,
        int rank,
        Platform platform
) {
    public static TrendResponse from(TrendRow row) {
        return new TrendResponse(
                row.id(),
                row.keyword(),
                row.rank(),
                Platform.valueOf(row.platform()) // String → enum
        );
    }
}
