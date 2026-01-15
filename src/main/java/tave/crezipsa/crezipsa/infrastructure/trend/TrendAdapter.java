package tave.crezipsa.crezipsa.infrastructure.trend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrendAdapter implements TrendQueryPort {

    private final @Qualifier("analyticsJdbc") NamedParameterJdbcTemplate analyticsJdbc;
    private final @Qualifier("mainJdbc") NamedParameterJdbcTemplate mainJdbc;

    @Override
    public List<TrendRow> findTopKeywordsByPlatformAndCategory(String platform, String category) {

        String sql;
        List<TrendRow> rows;

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

            rows = analyticsJdbc.query(sql, params, (rs, rowNum) -> new TrendRow(
                    rs.getLong("id"),
                    rs.getInt("category_rank"),
                    rs.getString("keyword"),
                    rs.getString("platform"),
                    rs.getString("category_name")
            ));
        }
        else {
            sql = """
                SELECT id, overall_rank, keyword, platform, category_name
                FROM analytics_keyword_virality
                WHERE platform = :platform 
                ORDER BY overall_rank ASC LIMIT :limit
            """;

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("platform", platform)
                    .addValue("limit", 10);

            rows = analyticsJdbc.query(sql, params, (rs, rowNum) -> new TrendRow(
                    rs.getLong("id"),
                    rs.getInt("overall_rank"),
                    rs.getString("keyword"),
                    rs.getString("platform"),
                    rs.getString("category_name")
            ));
        }

        return rows;
    }

    @Override
    public TrendDetailWithUrls findSelectedKeywordDetailBytrendId(long trendId) {

        String keywordSql = """
            SELECT id, overall_rank, category_rank, keyword, platform, category_name, virality_score
            FROM analytics_keyword_virality
            WHERE id = :id
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", trendId)
                .addValue("limit",10);

        TrendDetailRow detailRow = analyticsJdbc.queryForObject(keywordSql, params,
                (rs, rowNum) -> new TrendDetailRow(
                        rs.getLong("id"),
                        rs.getString("platform"),
                        rs.getString("category_name"),
                        rs.getInt("overall_rank"),
                        rs.getInt("category_rank"),
                        rs.getString("keyword"),
                        rs.getInt("virality_score")
                )
        );

        params.addValue("keyword", "%" + detailRow.keyword() + "%");

        String urlSql = """
            SELECT video_url, title, view_count
            FROM URLTABLE
            WHERE title LIKE :keyword
            ORDER BY created_at DESC
            LIMIT :limit
        """;
        // 검색 where 쿼리 후보: REPLACE(title, ' ', '') LIKE CONCAT('%', REPLACE(:keyword, ' ', ''), '%')

        List<TrendUrlRow> urlRows = analyticsJdbc.query(urlSql, params, (rs, rowNum) -> new TrendUrlRow(
                rs.getString(1),
                rs.getString(2),
                rs.getInt(3)
        ));

        return new TrendDetailWithUrls(detailRow, urlRows);
    }

    @Override
    public void saveTrend(long userId, TrendCommand trendCommand) {

        String Sql = """
           INSERT INTO keyword_storaged (user_id, keyword_id_from_analytics, keyword, category)
                       VALUES (:userId, :keywordIdFromAnalytics, :keyword, :category)    
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("keywordIdFromAnalytics",trendCommand.keywordId())
                .addValue("keyword",trendCommand.keyword())
                .addValue("category",trendCommand.category());

        mainJdbc.update(Sql, params);
    }

    @Override
    public TrendWithUrls findKeywordByKeyword(String keyword) {

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
                .addValue("keyword", "%" + keyword.trim() + "%")
                .addValue("limit",50);

        List<TrendRow> trends = analyticsJdbc.query(keywordSql, params, (rs, rowNum) -> new TrendRow(
                rs.getLong("keyword_id"),
                rs.getString("keyword"),
                rs.getString("category_id")));

        List<TrendUrlRow> urls = analyticsJdbc.query(urlSql, params, (rs, rowNum)  -> new TrendUrlRow(
                rs.getString("video_url"),
                rs.getString("title"),
                rs.getInt("view_count")));

        return new TrendWithUrls(trends, urls);

    }

}
