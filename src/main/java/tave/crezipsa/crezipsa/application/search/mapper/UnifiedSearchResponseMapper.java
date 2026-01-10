package tave.crezipsa.crezipsa.application.search.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import tave.crezipsa.crezipsa.application.search.dto.response.UnifiedSearchResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;

@Component
public class UnifiedSearchResponseMapper {

	public List<UnifiedSearchResponse.SearchItemResponse> toItems(
		List<Storyboard> storyboards,
		List<ChatRoom> chatRooms
	) {
		return Stream.concat(
			storyboards.stream().map(this::toStoryboardItem),
			chatRooms.stream().map(this::toChatRoomItem)
		)
			.toList();
	}

	private UnifiedSearchResponse.SearchItemResponse toStoryboardItem(Storyboard storyboard) {
		return new UnifiedSearchResponse.SearchItemResponse(
			storyboard.getId(),
			storyboard.getTitle(),
			UnifiedSearchResponse.SearchItemResponse.SearchType.STORYBOARD
		);
	}

	private UnifiedSearchResponse.SearchItemResponse toChatRoomItem(ChatRoom chatRoom) {
		return new UnifiedSearchResponse.SearchItemResponse(
			chatRoom.getId(),
			chatRoom.getTitle(),
			UnifiedSearchResponse.SearchItemResponse.SearchType.CHAT_ROOM
		);
	}

}
