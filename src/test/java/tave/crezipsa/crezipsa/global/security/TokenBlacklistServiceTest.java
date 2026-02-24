package tave.crezipsa.crezipsa.global.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class TokenBlacklistServiceTest {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@InjectMocks
	private TokenBlacklistService tokenBlacklistService;

	@Nested
	@DisplayName("blacklist")
	class Blacklist {

		@Test
		@DisplayName("남은 TTL이 양수이면 Redis에 blacklist 키를 저장한다")
		void blacklist_withPositiveTtl_savesToRedis() {
			// Given
			String token = "test-access-token";
			long remainingMs = 60000L;
			when(redisTemplate.opsForValue()).thenReturn(valueOperations);

			// When
			tokenBlacklistService.blacklist(token, remainingMs);

			// Then
			verify(valueOperations).set(
				eq("blacklist:" + token),
				eq("logout"),
				eq(remainingMs),
				eq(TimeUnit.MILLISECONDS)
			);
		}

		@Test
		@DisplayName("남은 TTL이 0 이하이면 Redis에 저장하지 않는다")
		void blacklist_withZeroOrNegativeTtl_doesNotSave() {
			// Given
			String token = "expired-token";
			long remainingMs = 0L;

			// When
			tokenBlacklistService.blacklist(token, remainingMs);

			// Then
			verify(redisTemplate, never()).opsForValue();
		}
	}

	@Nested
	@DisplayName("isBlacklisted")
	class IsBlacklisted {

		@Test
		@DisplayName("블랙리스트에 등록된 토큰이면 true를 반환한다")
		void isBlacklisted_whenTokenExists_returnsTrue() {
			// Given
			String token = "blacklisted-token";
			when(redisTemplate.hasKey("blacklist:" + token)).thenReturn(true);

			// When
			boolean result = tokenBlacklistService.isBlacklisted(token);

			// Then
			assertThat(result).isTrue();
		}

		@Test
		@DisplayName("블랙리스트에 없는 토큰이면 false를 반환한다")
		void isBlacklisted_whenTokenNotExists_returnsFalse() {
			// Given
			String token = "valid-token";
			when(redisTemplate.hasKey("blacklist:" + token)).thenReturn(false);

			// When
			boolean result = tokenBlacklistService.isBlacklisted(token);

			// Then
			assertThat(result).isFalse();
		}
	}
}
