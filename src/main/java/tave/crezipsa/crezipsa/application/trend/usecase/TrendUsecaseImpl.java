package tave.crezipsa.crezipsa.application.trend.usecase;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.trend.dto.request.TrendSaveRequest;
import tave.crezipsa.crezipsa.application.trend.dto.response.*;
import tave.crezipsa.crezipsa.application.trend.model.TrendItem;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.application.user.port.UserHistoryPort;
import tave.crezipsa.crezipsa.application.user.port.UserInterestPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TrendUsecaseImpl implements TrendUsecase {

    private final TrendQueryPort trendQueryPort;
    private final UserInterestPort userInterestPort;
    private final UserHistoryPort userHistoryPort;

	@Override
	@Transactional(readOnly = true)
	public List<TrendResponse> getTopTrends(String platform, String category) {
		if (platform == null || platform.isBlank()) {
			throw new CommonException(ErrorCode.USER_PLATFORM_NOT_SET);
		}

		List<TrendItem> trendRowList = trendQueryPort.findTopKeywordsByPlatformAndCategory(platform, category);
		return trendRowList.stream()
				.map(TrendResponse::from)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public TrendDetailResponse getTrendDetail(long trendId) {
		return TrendDetailResponse.from(trendQueryPort.findSelectedKeywordDetailByTrendId(trendId));
	}

	@Override
	public void saveTrend(long userId, TrendSaveRequest request) {
		trendQueryPort.saveTrend(userId, request.toCommand());
	}

	@Override
	@Transactional(readOnly = true)
	public TrendSearchResponse searchTrend(long userId, String keyword) {
		if (keyword == null) {
			throw new CommonException(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}
		String normalized = keyword.trim();
		if (normalized.isBlank()) {
			throw new CommonException(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}
		userHistoryPort.saveHistory(userId, normalized);
		return TrendSearchResponse.from(trendQueryPort.findKeywordByKeyword(normalized));
	}

	@Override
	@Transactional(readOnly = true)
	public List<KeywordResponse> getKeywordStoraged(long userId) {
		return trendQueryPort.findStoredKeywordsByUserId(userId).stream()
				.map(KeywordResponse::from)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TrendResponse> recommendTrendsByInterests(long userId) {
		List<String> userInterests = userInterestPort.getInterests(userId);
		List<TrendItem> trendRowList = trendQueryPort.findTopKeywordsByCategory(userInterests);

		return trendRowList.stream()
				.map(TrendResponse::from)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
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
