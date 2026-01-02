package tave.crezipsa.crezipsa.global.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AnalyticsJdbcConfig {

    @Bean@Qualifier("analyticsDataSourceProperties")
    @ConfigurationProperties("analytics.datasource")
    public DataSourceProperties analyticsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "analyticsDataSource")
    public DataSource analyticsDataSource(
            @Qualifier("analyticsDataSourceProperties") DataSourceProperties props
    ) {
        return props.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate analyticsJdbc(
            @Qualifier("analyticsDataSource") DataSource ds
    ) {
        return new NamedParameterJdbcTemplate(ds);
    }
}
