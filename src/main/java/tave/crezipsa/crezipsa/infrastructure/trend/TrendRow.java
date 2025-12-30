package tave.crezipsa.crezipsa.infrastructure.trend;

import tave.crezipsa.crezipsa.domain.user.enums.Platform;

public record TrendRow(
        Long id,
        int rank,
        String keyword,
        String platform
) {
}
