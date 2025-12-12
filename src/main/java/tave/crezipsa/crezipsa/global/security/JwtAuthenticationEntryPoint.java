package tave.crezipsa.crezipsa.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.JwtAuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 기본 에러코드 = INVALID_TOKEN
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;

        if (authException instanceof JwtAuthenticationException jwtEx) {
            errorCode = jwtEx.getErrorCode();
        }

        // GlobalResponseDto 형식으로 응답 생성
        GlobalResponseDto<?> body = GlobalResponseDto.fail(errorCode);

        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json;charset=UTF-8");

        // JSON 변환 후 응답
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
