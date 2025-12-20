package tave.crezipsa.crezipsa.infrastructure.chat.gemini;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Component
@RequiredArgsConstructor
public class GeminiStoryboardGenerator implements StoryboardGeneratorPort {

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.url}")
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
			.bodyToMono(Map.class)
			.map(response -> {
				var candidates = (List<Map<String, Object>>) response.get("candidates");
				if (candidates == null || candidates.isEmpty()) {
					return null;
				}

				var content = (Map<String, Object>) candidates.get(0).get("content");
				var parts = (List<Map<String, String>>) content.get("parts");

				return parts.get(0).get("text");
			})
			.block();

		if (!org.springframework.util.StringUtils.hasText(result)) {
			throw new CommonException(ErrorCode.GEMINI_EMPTY_RESPONSE);
		}

		return result;
	}
}
