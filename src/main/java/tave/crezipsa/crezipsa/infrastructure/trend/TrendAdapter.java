package tave.crezipsa.crezipsa.infrastructure.trend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.domain.trend.entity.KeywordStoraged;
import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;

import java.util.List;

@Repository
public class TrendAdapter implements TrendQueryPort {

	private final NamedParameterJdbcTemplate analyticsJdbc;
	private final NamedParameterJdbcTemplate mainJdbc;

	public TrendAdapter(
			@Qualifier("analyticsJdbc") NamedParameterJdbcTemplate analyticsJdbc,
			@Qualifier("mainJdbc") NamedParameterJdbcTemplate mainJdbc
	) {
		this.analyticsJdbc = analyticsJdbc;
		this.mainJdbc = mainJdbc;
	}

	@Override
	public List<TrendRow> findTopKeywordsByPlatformAndCategory(String platform, String category) {
		throw new UnsupportedOperationException("Not implemented: findTopKeywordsByPlatformAndCategory");
	}

	@Override
	public List<TrendRow> findTopKeywordsByCategory(List<String> categories) {
		throw new UnsupportedOperationException("Not implemented: findTopKeywordsByCategory");
	}

	@Override
	public TrendDetailWithUrls findSelectedKeywordDetailByTrendId(long trendId) {
		throw new UnsupportedOperationException("Not implemented: findSelectedKeywordDetailByTrendId");
	}

	@Override
	public void saveTrend(long userId, TrendCommand trendCommand) {
		throw new UnsupportedOperationException("Not implemented: saveTrend");
	}

	@Override
	public List<KeywordStoraged> findStoredKeywordsByUserId(long userId) {
		throw new UnsupportedOperationException("Not implemented: findStoredKeywordsByUserId");
	}

	@Override
	public TrendWithUrls findKeywordByKeyword(String keyword) {
		throw new UnsupportedOperationException("Not implemented: findKeywordByKeyword");
	}
}
