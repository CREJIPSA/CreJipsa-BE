package tave.crezipsa.crezipsa.domain.chat.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoom {

	private final Long id;
	private final Long userId;
	private final String title;
	private final LocalDateTime createdAt;

	public static ChatRoom create(Long userId, String title) {
		return ChatRoom.builder()
			.userId(userId)
			.title(title)
			.createdAt(LocalDateTime.now())
			.build();
	}
	public  ChatRoom changeTitle(String title) {
		return ChatRoom.builder()
			.id(this.id)
			.userId(this.userId)
			.title(title)
			.createdAt(this.createdAt)
			.build();
	}

}


