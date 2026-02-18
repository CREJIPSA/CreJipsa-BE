package tave.crezipsa.crezipsa.global.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tave.crezipsa.crezipsa.fixture.UserFixture.createUser;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.JwtAuthenticationException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenBlacklistService tokenBlacklistService;

	@Mock
	private FilterChain filterChain;

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("doFilterInternal")
	class DoFilterInternal {

		@Test
		@DisplayName("블랙리스트된 토큰이면 JwtAuthenticationException을 던진다")
		void blacklistedToken_throwsJwtAuthenticationException() {
			// Given
			String token = "blacklisted-token";
			request.addHeader("Authorization", "Bearer " + token);
			when(tokenBlacklistService.isBlacklisted(token)).thenReturn(true);

			// When & Then
			assertThatThrownBy(() ->
				jwtAuthenticationFilter.doFilterInternal(request, response, filterChain)
			)
				.isInstanceOf(JwtAuthenticationException.class)
				.satisfies(ex -> {
					JwtAuthenticationException jwtEx = (JwtAuthenticationException) ex;
					assertThat(jwtEx.getErrorCode()).isEqualTo(ErrorCode.TOKEN_BLACKLISTED);
				});

			verify(jwtTokenProvider).validateToken(token);
			verify(jwtTokenProvider, never()).getUserIdFromToken(token);
		}

		@Test
		@DisplayName("블랙리스트에 없는 유효한 토큰이면 인증을 설정한다")
		void validToken_setsAuthentication() throws Exception {
			// Given
			String token = "valid-token";
			Long userId = 1L;
			User user = createUser(userId);

			request.addHeader("Authorization", "Bearer " + token);
			when(tokenBlacklistService.isBlacklisted(token)).thenReturn(false);
			when(jwtTokenProvider.getUserIdFromToken(token)).thenReturn(userId);
			when(userRepository.findById(userId)).thenReturn(Optional.of(user));

			// When
			jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

			// Then
			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
			assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(user);
			verify(filterChain).doFilter(request, response);
		}

		@Test
		@DisplayName("Authorization 헤더가 없으면 인증을 설정하지 않는다")
		void noAuthHeader_skipsAuthentication() throws Exception {
			// Given — 헤더 없음

			// When
			jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

			// Then
			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
			verify(filterChain).doFilter(request, response);
		}
	}
}
