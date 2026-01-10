package tave.crezipsa.crezipsa.infrastructure.trend;

import java.util.List;

public record TrendDetailWithUrls(
        TrendDetailRow detailRow,
        List<TrendUrlRow> urls
) {
}
