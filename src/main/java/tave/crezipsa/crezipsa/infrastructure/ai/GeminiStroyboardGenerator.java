package tave.crezipsa.crezipsa.infrastructure.ai;

import org.springframework.stereotype.Component;

import tave.crezipsa.crezipsa.domain.chat.port.StoryboardGeneratorPort;

@Component
public class GeminiStroyboardGenerator implements StoryboardGeneratorPort {

	@Override
	public String generate(String prompt) {
		return "임시 구현";
	}
}
