package tave.crezipsa.crezipsa.infrastructure.chat.gemini;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.chat.dto.response.StoryboardStructuredResponse;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardStructurerPort;

@Component
@RequiredArgsConstructor
public class GeminiStroyboardStructurer implements StoryboardStructurerPort {

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.url}")
	private String url;

	private final WebClient webClient = WebClient.create();

	@Override
	public StoryboardStructuredResponse structure(String aiMessageContent) {

		String prompt = """
        아래 텍스트를 영상 스토리보드 형태로 구조화해줘.
        반드시 아래 4개 필드를 모두 채워서 출력해.
        출력 형식은 JSON만. 다른 말 하지마.

        {
          "cutSummary": "...",
          "script": "...",
          "caption": "...",
          "time": "..."
        }

        텍스트:
        """ + aiMessageContent;

		Map<String, Object> body = Map.of(
			"contents", new Object[]{
				Map.of("parts", new Object[]{
					Map.of("text", prompt)
				})
			}
		);

		String jsonText = webClient.post()
			.uri(url + "?key=" + apiKey)
			.bodyValue(body)
			.retrieve()
			.bodyToMono(Map.class)
			.map(response -> {
				var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
				var content = (Map<String, Object>) candidates.get(0).get("content");
				var parts = (java.util.List<Map<String, String>>) content.get("parts");
				return parts.get(0).get("text"); // JSON 문자열이 오게 유도
			})
			.block();

		// JSON 파싱 (Jackson)
		try {
			com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
			Map<String, String> m = om.readValue(jsonText, new com.fasterxml.jackson.core.type.TypeReference<>() {});
			return new StoryboardStructuredResponse(
				m.get("cutSummary"),
				m.get("script"),
				m.get("caption"),
				m.get("time")
			);
		} catch (Exception e) {
			// 실패시 원문을 script에 넣는 등 fallback 가능
			return new StoryboardStructuredResponse(
				null,
				aiMessageContent,
				null,
				null
			);
		}
	}
}


