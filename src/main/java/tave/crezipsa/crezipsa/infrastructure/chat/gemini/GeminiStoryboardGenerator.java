package tave.crezipsa.crezipsa.infrastructure.chat.gemini;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiStoryboardGenerator implements StoryboardGeneratorPort {

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
	private String url;

	private final WebClient webClient = WebClient.create();

	@Override
	public String generate(String prompt) {

		Map<String, Object> body = Map.of(
			"contents", new Object[]{
				Map.of(
					"parts", new Object[]{
						Map.of("text", prompt)
					}
				)
			}
		);

		String result = webClient.post()
			.uri(url + "?key=" + apiKey)
			.bodyValue(body)
			.retrieve()
			.onStatus(
				status -> status.is4xxClientError(),
				response -> response.bodyToMono(String.class)
					.doOnNext(errorBody -> log.error("Gemini 4xx response body={}", body))
					.then(Mono.error(new CommonException(ErrorCode.GEMINI_CLIENT_ERROR)))
			)
			.onStatus(
				status -> status.is5xxServerError(),
				response -> response.bodyToMono(String.class)
					.doOnNext(errorBody -> log.error("Gemini 5xx response body={}", body))
					.then(Mono.error(new CommonException(ErrorCode.GEMINI_SERVER_ERROR)))
			)
			.bodyToMono(Map.class)
			.map(response -> {
				var candidates = (List<Map<String, Object>>) response.get("candidates");
				if (candidates == null || candidates.isEmpty()) {
					throw new CommonException(ErrorCode.GEMINI_EMPTY_RESPONSE);
				}

				var content = (Map<String, Object>) candidates.get(0).get("content");
				var parts = (List<Map<String, String>>) content.get("parts");

				if (parts == null || parts.isEmpty()
					|| !org.springframework.util.StringUtils.hasText(parts.get(0).get("text"))) {
					throw new CommonException(ErrorCode.GEMINI_EMPTY_RESPONSE);
				}


				return parts.get(0).get("text");
			})
			.onErrorMap(
				WebClientResponseException.class,
				ex -> new CommonException(ErrorCode.GEMINI_REQUEST_FAILED)
			)
			.block();

		if (!org.springframework.util.StringUtils.hasText(result)) {
			throw new CommonException(ErrorCode.GEMINI_EMPTY_RESPONSE);
		}

		return result;
	}
}
