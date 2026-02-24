package tave.crezipsa.crezipsa.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class TestRedisConfig {

	@SuppressWarnings("unchecked")
	@Bean
	@ConditionalOnMissingBean(RedisTemplate.class)
	public RedisTemplate<String, String> redisTemplate() {
		return mock(RedisTemplate.class);
	}
}
