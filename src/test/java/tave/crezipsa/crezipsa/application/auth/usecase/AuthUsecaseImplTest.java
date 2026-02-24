package tave.crezipsa.crezipsa.application.auth.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tave.crezipsa.crezipsa.fixture.AuthFixture.createAuth;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tave.crezipsa.crezipsa.application.auth.dto.request.RefreshTokenRequest;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.global.exception.model.JwtAuthenticationException;
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
	@DisplayName("reissueAccessToken")
	class ReissueAccessToken {

		@Test
		@DisplayName("유효한 RefreshToken으로 요청하면 AccessToken과 RefreshToken이 모두 갱신된다")
		void reissueAccessToken_success_rotatesBothTokens() {
			// Given
			Long userId = 1L;
			String oldRefreshToken = "old-refresh-token";
			String newAccessToken = "new-access-token";
			String newRefreshToken = "new-refresh-token";

			Auth auth = createAuth(userId, "old-access-token", oldRefreshToken);
			RefreshTokenRequest request = new RefreshTokenRequest(userId, oldRefreshToken);

			when(authRepository.findByUserIdAndRefreshToken(userId, oldRefreshToken))
				.thenReturn(Optional.of(auth));
			when(tokenProvider.generateAccessToken(userId)).thenReturn(newAccessToken);
			when(tokenProvider.generateRefreshToken(userId)).thenReturn(newRefreshToken);

			// When
			TokenResponse response = authUsecase.reissueAccessToken(request);

			// Then
			assertThat(response.accessToken()).isEqualTo(newAccessToken);
			assertThat(response.refreshToken()).isEqualTo(newRefreshToken);
		}

		@Test
		@DisplayName("DB에 존재하지 않는 RefreshToken으로 요청하면 INVALID_REFRESH_TOKEN 예외가 발생한다")
		void reissueAccessToken_invalidRefreshToken_throwsException() {
			// Given
			Long userId = 1L;
			String invalidRefreshToken = "invalid-refresh-token";
			RefreshTokenRequest request = new RefreshTokenRequest(userId, invalidRefreshToken);

			when(authRepository.findByUserIdAndRefreshToken(userId, invalidRefreshToken))
				.thenReturn(Optional.empty());

			// When & Then
			assertThatThrownBy(() -> authUsecase.reissueAccessToken(request))
				.isInstanceOf(CommonException.class)
				.hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REFRESH_TOKEN);
		}

		@Test
		@DisplayName("만료된 RefreshToken으로 요청하면 JwtAuthenticationException이 발생한다")
		void reissueAccessToken_expiredRefreshToken_throwsException() {
			// Given
			Long userId = 1L;
			String expiredRefreshToken = "expired-refresh-token";
			RefreshTokenRequest request = new RefreshTokenRequest(userId, expiredRefreshToken);

			doThrow(new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED))
				.when(tokenProvider).validateToken(expiredRefreshToken);

			// When & Then
			assertThatThrownBy(() -> authUsecase.reissueAccessToken(request))
				.isInstanceOf(JwtAuthenticationException.class)
				.hasFieldOrPropertyWithValue("errorCode", ErrorCode.TOKEN_EXPIRED);
		}
	}

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
