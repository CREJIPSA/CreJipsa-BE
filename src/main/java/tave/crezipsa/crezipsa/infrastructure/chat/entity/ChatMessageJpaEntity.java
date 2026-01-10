package tave.crezipsa.crezipsa.infrastructure.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;

@Entity
@Table(name = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessageJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;

	private Long chatRoomId;

	@Enumerated(EnumType.STRING)
	private SenderType senderType;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	public enum SenderType {
		USER, AI
	}

}
