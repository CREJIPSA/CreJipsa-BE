package tave.crezipsa.crezipsa.infrastructure.trend;

import java.util.List;

public record TrendDetailRow(
            Long id,
            String platform,
            String category,
            int overall_rank,
            int category_rank,
            String keyword,
            double viralityScore
    ) {
    }

