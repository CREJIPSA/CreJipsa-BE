package tave.crezipsa.crezipsa.infrastructure.trend;

import lombok.AllArgsConstructor;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;


public record TrendRow(
        Long id,
        int rank,
        String keyword,
        String platform,
        String category
) {
    public TrendRow(long id, String keyword, String category) {
        this(id, 0, keyword, null, category);
    }
}
