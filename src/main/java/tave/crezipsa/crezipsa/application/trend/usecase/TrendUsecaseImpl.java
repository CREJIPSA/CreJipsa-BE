package tave.crezipsa.crezipsa.application.trend.usecase;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.trend.dto.response.*;
import tave.crezipsa.crezipsa.application.trend.dto.response.request.TrendRequest;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.application.user.port.UserHistoryPort;
import tave.crezipsa.crezipsa.application.user.port.UserInterestPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendDetailWithUrls;
import tave.crezipsa.crezipsa.infrastructure.trend.TrendRow;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TrendUsecaseImpl implements TrendUsecase {

    private final TrendQueryPort trendQueryPort;
    private final UserInterestPort userInterestPort;
    private final UserHistoryPort userHistoryPort;

    @Override
    public List<TrendResponse> getTopTrends(String platform, String category) {

        if(platform.isEmpty() ||platform.isBlank()){
            throw new CommonException(ErrorCode.USER_PLATFORM_NOT_SET);
        }

        List<TrendRow> trendRowList = trendQueryPort.findTopKeywordsByPlatformAndCategory(platform, category);
        return trendRowList.stream()
                .map(TrendResponse::from)
                .toList();
    }

    @Override
    public TrendDetailResponse getTrendDetail(long trendId) {

        TrendDetailWithUrls trendDetailWithUrls = trendQueryPort.findSelectedKeywordDetailBytrendId(trendId);
        return TrendDetailResponse.from(trendDetailWithUrls);
    }

    @Override
    public void saveTrend(long userId, TrendRequest request) {

        trendQueryPort.saveTrend(userId, request.from(request));
    }

    @Override
    public TrendSearchResponse searchTrend(long userId, String keyword) {
        userHistoryPort.saveHistory(userId,keyword);
        return TrendSearchResponse.from(trendQueryPort.findKeywordByKeyword(keyword));
    }

    @Override
    public List<KeywordResponse> getKeywordStoraged(long userId) {
        return trendQueryPort.findStoredKeywordsByUserId(userId).stream()
                .map(KeywordResponse::from)
                .toList();
    }

    @Override
    public List<TrendResponse> recommendTrendsByInterests(long userId) {
        List<String> userInterests = userInterestPort.getInterests(userId);
        List<TrendRow> trendRowList = trendQueryPort.findTopKeywordsByCategory(userInterests);

        return trendRowList.stream()
                .map(TrendResponse::from)
                .toList();
    }

    @Override
    public List<UserHistoryResponse> getUserHistory(Long userId) {
        return userHistoryPort.getUserHistory(userId).stream()
                .map(UserHistoryResponse::from)
                .toList();
    }

    @Override
    public void deleteOneUserHistory(long userId, int historyId) {
        userHistoryPort.deleteHistoryByHistoryId(userId, historyId);
    }

    @Override
    public void deleteAllUserHistory(long userId) {
        userHistoryPort.deleteAllHistoryByUserId(userId);
    }
}
