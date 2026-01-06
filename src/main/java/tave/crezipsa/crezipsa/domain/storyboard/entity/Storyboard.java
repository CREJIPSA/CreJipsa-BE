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
	private final LocalDateTime createdAt;

	public static Storyboard create(Long userId, String title) {
		return Storyboard.builder()
			.userId(userId)
			.title((title == null || title.isBlank()) ? "스토리보드 제목" : title)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public Storyboard withTitle(String title) {
		return Storyboard.builder()
			.id(this.id)
			.userId(this.userId)
			.title(title)
			.createdAt(this.createdAt)
			.build();
	}
}
