package tave.crezipsa.crezipsa.application.auth.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;
import tave.crezipsa.crezipsa.global.security.TokenBlacklistService;

@ExtendWith(MockitoExtension.class)
class AuthUsecaseImplTest {

	@Mock
	private AuthRepository authRepository;

	@Mock
	private JwtTokenProvider tokenProvider;

	@Mock
	private TokenBlacklistService tokenBlacklistService;

	@InjectMocks
	private AuthUsecaseImpl authUsecase;

	@Nested
	@DisplayName("deleteToken")
	class DeleteToken {

		@Test
		@DisplayName("Auth가 존재하면 accessToken을 블랙리스트에 등록하고 삭제한다")
		void deleteToken_withAuth_blacklistsAndDeletes() {
			// Given
			Long userId = 1L;
			String accessToken = "test-access-token";
			long remainingMs = 60000L;

			Auth auth = Auth.builder()
				.authId(1L)
				.userId(userId)
				.accessToken(accessToken)
				.refreshToken("test-refresh-token")
				.build();

			when(authRepository.findByUserId(userId)).thenReturn(Optional.of(auth));
			when(tokenProvider.getRemainingExpiration(accessToken)).thenReturn(remainingMs);

			// When
			authUsecase.deleteToken(userId);

			// Then
			verify(tokenBlacklistService).blacklist(accessToken, remainingMs);
			verify(authRepository).deleteByUserId(userId);
		}

		@Test
		@DisplayName("Auth가 존재하지 않으면 블랙리스트 등록 없이 삭제만 수행한다")
		void deleteToken_withoutAuth_onlyDeletes() {
			// Given
			Long userId = 1L;
			when(authRepository.findByUserId(userId)).thenReturn(Optional.empty());

			// When
			authUsecase.deleteToken(userId);

			// Then
			verify(authRepository).deleteByUserId(userId);
		}
	}
}
