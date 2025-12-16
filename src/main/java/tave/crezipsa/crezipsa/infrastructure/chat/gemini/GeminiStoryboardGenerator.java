package tave.crezipsa.crezipsa.infrastructure.chat.gemini;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;

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

		return webClient.post()
			.uri(url + "?key=" + apiKey)
			.bodyValue(body)
			.retrieve()
			.bodyToMono(Map.class)
			.map(response -> {
				var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
				var content = (Map<String, Object>) candidates.get(0).get("content");
				var parts = (java.util.List<Map<String, String>>) content.get("parts");
				return parts.get(0).get("text");
			})
			.block();
	}
}
