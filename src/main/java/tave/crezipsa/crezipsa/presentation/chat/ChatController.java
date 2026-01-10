package tave.crezipsa.crezipsa.presentation.chat;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.chat.dto.request.ChangeChatRoomTitleRequest;
import tave.crezipsa.crezipsa.application.chat.dto.request.ChatMessageRequest;
import tave.crezipsa.crezipsa.application.chat.dto.response.ChatMessageResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.ChatRoomListResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;
import tave.crezipsa.crezipsa.application.chat.usecase.ChatUseCase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

	private final ChatUseCase chatUseCase;

	@PostMapping
	public GlobalResponseDto<Long> createChatRoom(
		@AuthenticationPrincipal User user,
		@RequestParam(required = false) String title
	) {
		Long chatRoomId = chatUseCase.createChatRoom(
			user.getUserId(),
			title
		);

		return GlobalResponseDto.success(chatRoomId);
	}

	@PostMapping("/{chatRoomId}/messages")
	public GlobalResponseDto<GeminiChatResponse> sendMessage(
		@AuthenticationPrincipal User user,
		@PathVariable Long chatRoomId,
		@Validated @RequestBody ChatMessageRequest request
	) {
		GeminiChatResponse response =
			chatUseCase.sendUserMessage(
				chatRoomId,
				user.getUserId(),
				request.message()
			);

		return GlobalResponseDto.success(response);
	}

	@GetMapping
	public GlobalResponseDto<List<ChatRoomListResponse>> getMyChatRooms(
		@AuthenticationPrincipal User user
	) {
		return GlobalResponseDto.success(
			chatUseCase.getMyChatRooms(user.getUserId())
		);
	}

	@GetMapping("/{chatRoomId}")
	public GlobalResponseDto<ChatMessageResponse> getChatDetail(
		@AuthenticationPrincipal User user,
		@PathVariable Long chatRoomId
	) {
		return GlobalResponseDto.success(
			chatUseCase.getChatDetail(
				user.getUserId(),
				chatRoomId
			)
		);
	}

	@PatchMapping("/{chatRoomId}/title")
	public GlobalResponseDto<Void> changeChatRoomTitle(
		@AuthenticationPrincipal User user,
		@PathVariable Long chatRoomId,
		@Validated @RequestBody ChangeChatRoomTitleRequest request
	) {
		chatUseCase.changeChatRoomTitle(
			user.getUserId(),
			chatRoomId,
			request.title()
		);
		return GlobalResponseDto.success();
	}





}
