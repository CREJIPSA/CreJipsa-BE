package tave.crezipsa.crezipsa.infrastructure.trend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import tave.crezipsa.crezipsa.application.trend.port.TrendQueryPort;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrendAdapter implements TrendQueryPort {

    private final @Qualifier("analyticsJdbc") NamedParameterJdbcTemplate jdbc;

    @Override
    public List<TrendRow> findTopKeywordsByPlatform(String platform) {

        String sql = """
            SELECT id, `rank` AS keyword_rank, keyword, platform
            FROM analytics_top_keywords
            WHERE platform = :platform
            ORDER BY keyword_rank ASC
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("platform", platform);

        List<TrendRow> rows = jdbc.query(sql, params, (rs, rowNum) -> new TrendRow(
                rs.getLong("id"),
                rs.getInt("keyword_rank"),
                rs.getString("keyword"),
                rs.getString("platform")
        ));

        System.out.println("MAPPED SIZE=" + rows.size());
        System.out.println("MAPPED=" + rows);

        return rows;
    }
}
