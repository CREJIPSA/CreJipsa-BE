package tave.crezipsa.crezipsa.application.trend.port;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;

import java.util.List;

public interface TrendQueryPort {
    List<TrendRow> findTopKeywordsByPlatform(String platform);
}
