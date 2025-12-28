package tave.crezipsa.crezipsa.application.trend.usecase;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TrendUsecaseImpl implements TrendUsecase {

    private final TrendQueryPort trendQueryPort;

    @Override
    public List<TrendResponse> execute(String platform) {

        System.out.println(platform + "\n\n");

        List<TrendRow> trendRowList = trendQueryPort.findTopKeywordsByPlatform(platform);
        return trendRowList.stream()
                .map(TrendResponse::from)
                .toList();
    }

}
