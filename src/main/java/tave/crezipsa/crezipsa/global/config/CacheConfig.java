package tave.crezipsa.crezipsa.global.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.activateDefaultTyping(
				BasicPolymorphicTypeValidator.builder()
					.allowIfBaseType("tave.crezipsa.crezipsa.")
					.allowIfBaseType("java.util.")
					.allowIfBaseType("java.time.")
					.build(),
				ObjectMapper.DefaultTyping.NON_FINAL
			);

		GenericJackson2JsonRedisSerializer jsonSerializer =
			new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(5))
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
			.disableCachingNullValues();

		Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
		cacheConfigs.put("community-list", defaultConfig);
		cacheConfigs.put("community-list-by-field", defaultConfig);
		cacheConfigs.put("community-detail", defaultConfig);
		cacheConfigs.put("comment-list", defaultConfig);

		return RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(defaultConfig)
			.withInitialCacheConfigurations(cacheConfigs)
			.build();
	}
}
