package tave.crezipsa.crezipsa.infrastructure.chat.gemini;

import java.net.URI;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardStructuredResponse;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardStructurerPort;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiStroyboardStructurer implements StoryboardStructurerPort {

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
	private String url;

	private final WebClient webClient = WebClient.create();
	private final ObjectMapper om = new ObjectMapper();

	@Override
	public StoryboardStructuredResponse structure(String aiMessageContent) {

		// 프롬프트: JSON 포맷을 강제하는 강력한 지시사항
		String prompt = """
        역할: 너는 전문 영상 기획자다.
        임무: 아래 입력된 텍스트 내용을 바탕으로 영상 촬영을 위한 '스토리보드' 정보 하나를 추출해라.
        
        [출력 포맷]
        반드시 아래 JSON 형식으로만 출력해. 마크다운 코드 블록(```json)이나 잡담은 절대 포함하지 마.
        {
          "cutSummary": "해당 컷의 핵심 요약 (1문장)",
          "script": "영상에서 실제로 말해야 할 대사나 내레이션",
          "caption": "화면에 띄울 자막 내용",
          "time": "예상 소요 시간 (예: 5초)"
        }

        [입력 텍스트]
        """ + aiMessageContent;

		String safeUrl = url.trim();
		String safeKey = apiKey.trim();

		// ★ 중요: URI 객체로 변환하여 인코딩 방지
		URI uri = URI.create(safeUrl + "?key=" + safeKey);

		Map<String, Object> body = Map.of(
			"contents", new Object[]{
				Map.of("parts", new Object[]{
					Map.of("text", prompt)
				})
			}
		);

		// 1. 요청 및 원본 텍스트 수신
		String rawResponse = webClient.post()
			.uri(uri)
			.header("Content-Type", "application/json")
			.bodyValue(body)
			.retrieve()
			.bodyToMono(Map.class)
			.map(response -> {
				try {
					var candidates = (List<Map<String, Object>>) response.get("candidates");
					var content = (Map<String, Object>) candidates.get(0).get("content");
					var parts = (List<Map<String, String>>) content.get("parts");
					return parts.get(0).get("text");
				} catch (Exception e) {
					log.error("Gemini Structurer 응답 파싱 실패: {}", response);
					throw new RuntimeException("Gemini Parsing Error");
				}
			})
			.block();

		// 2. 응답 정제 (마크다운 제거)
		String cleanedJson = cleanJson(rawResponse);
		log.info("Gemini 정제된 응답: {}", cleanedJson);

		// 3. JSON 변환
		try {
			Map<String, String> m = om.readValue(cleanedJson, new TypeReference<>() {});
			return new StoryboardStructuredResponse(
				m.get("cutSummary"),
				m.get("script"),
				m.get("caption"),
				m.get("time")
			);
		} catch (Exception e) {
			log.error("JSON 변환 실패. 원본: {}", rawResponse, e);
			// 실패 시 Fallback: 원본 내용을 script에 저장
			return new StoryboardStructuredResponse(
				"구조화 실패",
				aiMessageContent,
				null,
				null
			);
		}
	}

	// 마크다운 제거 메서드
	private String cleanJson(String text) {
		if (text == null || text.isBlank()) return "{}";
		String cleaned = text.trim();
		if (cleaned.startsWith("```json")) {
			cleaned = cleaned.substring(7);
		} else if (cleaned.startsWith("```")) {
			cleaned = cleaned.substring(3);
		}
		if (cleaned.endsWith("```")) {
			cleaned = cleaned.substring(0, cleaned.length() - 3);
		}
		return cleaned.trim();
	}
}