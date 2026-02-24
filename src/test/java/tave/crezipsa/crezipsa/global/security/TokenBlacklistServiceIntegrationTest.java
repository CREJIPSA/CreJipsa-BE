package tave.crezipsa.crezipsa.global.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("redis-test")
@Testcontainers
class TokenBlacklistServiceIntegrationTest {

	@Container
	static GenericContainer<?> redis =
		new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
			.withExposedPorts(6379);

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redis::getHost);
		registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
	}

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@Test
	@DisplayName("실제 Redis에 블랙리스트 등록 후 조회되고 TTL 만료 후 사라진다")
	void blacklistAndQuery_withRealRedis() throws InterruptedException {
		// Given
		String token = "real-test-token";

		// When
		tokenBlacklistService.blacklist(token, 2000L);

		// Then - 등록 직후 조회
		assertThat(tokenBlacklistService.isBlacklisted(token)).isTrue();

		// Then - TTL 만료 후 조회
		Thread.sleep(2500L);
		assertThat(tokenBlacklistService.isBlacklisted(token)).isFalse();
	}
}
