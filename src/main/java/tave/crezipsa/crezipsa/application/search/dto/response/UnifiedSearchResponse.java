package tave.crezipsa.crezipsa.application.search.dto.response;

import java.util.List;

public record UnifiedSearchResponse(
	String keyword,
	boolean empty,
	List<SearchItemResponse> items
) {

	public record SearchItemResponse(
		Long id,
		String title,
		SearchType type
	){}

	public enum SearchType {
		STORYBOARD,
		CHAT_ROOM
	}

}
