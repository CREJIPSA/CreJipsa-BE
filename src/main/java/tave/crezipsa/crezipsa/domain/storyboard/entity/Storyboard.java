package tave.crezipsa.crezipsa.domain.storyboard.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Storyboard {

	private final Long id;
	private final Long userId;

	private final String title;
	private final String content;

	private final LocalDateTime createdAt;

	public static Storyboard create(Long userId, String title, String content) {
		return Storyboard.builder()
			.userId(userId)
			.title(title)
			.content(content)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
