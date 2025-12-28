package tave.crezipsa.crezipsa.presentation.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test/gemini")
public class GeminiTestController {

	private final StoryboardGeneratorPort storyboardGeneratorPort;

	@PostMapping
	public String test(@RequestBody String prompt) {
		return storyboardGeneratorPort.generate(prompt);
	}
}
