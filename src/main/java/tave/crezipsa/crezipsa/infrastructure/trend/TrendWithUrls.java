package tave.crezipsa.crezipsa.infrastructure.trend;

import java.util.List;

public record TrendWithUrls(
        List<TrendRow> rows,
        List<TrendUrlRow> urls
) {
}
