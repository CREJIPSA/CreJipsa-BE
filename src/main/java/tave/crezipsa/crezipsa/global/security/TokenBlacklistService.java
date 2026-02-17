package tave.crezipsa.crezipsa.global.security;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

	private static final String BLACKLIST_PREFIX = "blacklist:";

	private final RedisTemplate<String, String> redisTemplate;

	public void blacklist(String token, long remainingMs) {
		if (remainingMs > 0) {
			redisTemplate.opsForValue()
				.set(BLACKLIST_PREFIX + token, "logout", remainingMs, TimeUnit.MILLISECONDS);
		}
	}

	public boolean isBlacklisted(String token) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
	}
}
