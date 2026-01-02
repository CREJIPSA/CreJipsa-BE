package tave.crezipsa.crezipsa.application.trend.usecase;

import tave.crezipsa.crezipsa.application.trend.dto.response.TrendResponse;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.util.List;


public interface TrendUsecase {

    List<TrendResponse> execute(String platform);
}
