package tave.crezipsa.crezipsa.global.security;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

	private static final String BLACKLIST_PREFIX = "blacklist:";

	private final RedisTemplate<String, String> redisTemplate;

	public void blacklist(String token, long remainingMs) {
		if (remainingMs <= 0) {
			return;
		}
		try {
			redisTemplate.opsForValue()
				.set(BLACKLIST_PREFIX + token, "logout", remainingMs, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.warn("Redis 블랙리스트 등록 실패 — 토큰은 자연 만료까지 유효합니다: {}", e.getMessage());
		}
	}

	public boolean isBlacklisted(String token) {
		try {
			return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
		} catch (Exception e) {
			log.warn("Redis 블랙리스트 조회 실패 — 요청을 허용합니다: {}", e.getMessage());
			return false;
		}
	}
}
