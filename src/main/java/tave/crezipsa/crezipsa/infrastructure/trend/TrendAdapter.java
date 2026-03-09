package tave.crezipsa.crezipsa.infrastructure.trend;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.application.trend.model.TrendDetail;
import tave.crezipsa.crezipsa.application.trend.model.TrendDetailResult;
import tave.crezipsa.crezipsa.application.trend.model.TrendItem;
import tave.crezipsa.crezipsa.application.trend.model.TrendSearchResult;
import tave.crezipsa.crezipsa.application.trend.model.TrendUrl;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.domain.trend.entity.KeywordStoraged;
import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrendAdapter implements TrendQueryPort {

	private final @Qualifier("analyticsJdbc") NamedParameterJdbcTemplate analyticsJdbc;
	private final @Qualifier("mainJdbc") NamedParameterJdbcTemplate mainJdbc;

	@Override
	public List<TrendItem> findTopKeywordsByPlatformAndCategory(String platform, String category) {

		String sql;
		List<TrendItem> rows;

		if (category != null && !category.isBlank()) {

			sql = """
                SELECT id, category_rank, keyword, platform, category_name
                FROM analytics_keyword_virality
                WHERE platform = :platform AND category_name = :category
                ORDER BY category_rank ASC LIMIT :limit
            """;

			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("platform", platform)
					.addValue("limit",10)
					.addValue("category", category);

			rows = analyticsJdbc.query(sql, params, (rs, rowNum) -> new TrendItem(
					rs.getLong("id"),
					rs.getInt("category_rank"),
					rs.getString("keyword"),
					rs.getString("platform"),
					rs.getString("category_name"),
					null
			));
		}
		else {
			sql = """
                SELECT id, overall_rank, keyword, platform, category_name, trend_direction
                FROM analytics_keyword_virality
                WHERE platform = :platform 
                ORDER BY overall_rank ASC LIMIT :limit
            """;

			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("platform", platform)
					.addValue("limit", 10);

			rows = analyticsJdbc.query(sql, params, (rs, rowNum) -> new TrendItem(
					rs.getLong("id"),
					rs.getInt("overall_rank"),
					rs.getString("keyword"),
					rs.getString("platform"),
					rs.getString("category_name"),
					rs.getString("trend_direction")
			));
		}

		return rows;
	}

	@Override
	public List<TrendItem> findTopKeywordsByCategory(List<String> categories) {

		String sql = """
        SELECT id, keyword,category_name
        FROM analytics_keyword_virality
        WHERE category_name = :category
        ORDER BY category_rank ASC
        LIMIT :limit
    """;

		List<TrendItem> result = new ArrayList<>();

		for (String category : categories) {
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("category", category)
					.addValue("limit", 10);

			List<TrendItem> rows = analyticsJdbc.query(sql, params, (rs, rowNum) -> new TrendItem(
					rs.getLong("id"),
					0,
					rs.getString("keyword"),
					null,
					rs.getString("category_name"),
					null
			));

			result.addAll(rows);
		}

		return result;
	}

	@Override
	public TrendDetailResult findSelectedKeywordDetailByTrendId(long trendId) {

		String keywordSql = """
            SELECT id, overall_rank, category_rank, keyword, platform, category_name, virality_score
            FROM analytics_keyword_virality
            WHERE id = :id
        """;

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("id", trendId)
				.addValue("limit",10);

		TrendDetail detail;
		try {
			detail = analyticsJdbc.queryForObject(keywordSql, params,
					(rs, rowNum) -> new TrendDetail(
							rs.getLong("id"),
							rs.getString("platform"),
							rs.getString("category_name"),
							rs.getInt("overall_rank"),
							rs.getInt("category_rank"),
							rs.getString("keyword"),
							rs.getDouble("virality_score")
					)
			);
		}
		catch (EmptyResultDataAccessException e) {
			throw new CommonException(ErrorCode.TREND_NOT_FOUND);
		}

		params.addValue("keyword", "%" + detail.keyword() + "%");

		String urlSql = """
            SELECT video_url, title, view_count
            FROM URLTABLE
            WHERE title LIKE :keyword
            ORDER BY created_at DESC
            LIMIT :limit
        """;
		// 검색 where 쿼리 후보: REPLACE(title, ' ', '') LIKE CONCAT('%', REPLACE(:keyword, ' ', ''), '%')

		List<TrendUrl> urls = analyticsJdbc.query(urlSql, params, (rs, rowNum) -> new TrendUrl(
				rs.getString(1),
				rs.getString(2),
				rs.getInt(3)
		));

		return new TrendDetailResult(detail, urls);
	}

	@Override
	public void saveTrend(long userId, TrendCommand trendCommand) {

		String sql = """
            INSERT INTO keyword_storaged (user_id, keyword_id_from_analytics, keyword, category)
                        VALUES (:userId, :keywordIdFromAnalytics, :keyword, :category)    
         """;

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("keywordIdFromAnalytics",trendCommand.keywordId())
				.addValue("keyword",trendCommand.keyword())
				.addValue("category",trendCommand.category());

		mainJdbc.update(sql, params);
	}

	@Override
	public List<KeywordStoraged> findStoredKeywordsByUserId(long userId) {

		String sql = """
        SELECT keyword_storage_id, keyword, category,created_at
        FROM keyword_storaged
        WHERE user_id = :userId
        ORDER BY created_at DESC
    """;

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId);

		return mainJdbc.query(sql, params, (rs, rowNum) -> KeywordStoraged.builder()
				.keywordStoragedId(rs.getLong("keyword_storage_id"))
				.keyword(rs.getString("keyword"))
				.category(rs.getString("category"))
				.createdAt(rs.getTimestamp("created_at").toLocalDateTime().toLocalDate())
				.build()
		);
	}

	@Override
	public TrendSearchResult findKeywordByKeyword(String keyword) {
		String normalized = keyword == null ? "" : keyword.trim();
		if (normalized.isBlank()) {
			throw new CommonException(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		String keywordSql = """
           WITH filtered AS (
                            SELECT keyword_id, keyword, category_id
                            FROM raw_keywords
                            WHERE keyword LIKE :keyword
                        ),
                        dedup AS (
                            SELECT
                                keyword_id,
                                keyword,
                                category_id,
                                ROW_NUMBER() OVER (
                                    PARTITION BY keyword
                                    ORDER BY keyword_id DESC
                                ) AS rn
                            FROM filtered
                        )
                        SELECT keyword_id, keyword, category_id
                        FROM dedup
                        WHERE rn = 1
                        ORDER BY keyword_id DESC
                        LIMIT :limit;
                        
        """;

		String urlSql = """
            SELECT video_url, title, view_count
            FROM URLTABLE
            WHERE title LIKE :keyword
            ORDER BY created_at DESC
            LIMIT :limit
        """;

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("keyword", "%" + normalized + "%")
				.addValue("limit",50);

		List<TrendItem> trends = analyticsJdbc.query(keywordSql, params, (rs, rowNum) -> new TrendItem(
				rs.getLong("keyword_id"),
				0,
				rs.getString("keyword"),
				null,
				rs.getString("category_id"),
				null
		));

		List<TrendUrl> urls = analyticsJdbc.query(urlSql, params, (rs, rowNum)  -> new TrendUrl(
				rs.getString("video_url"),
				rs.getString("title"),
				rs.getInt("view_count")
		));

		return new TrendSearchResult(trends, urls);

	}

}
