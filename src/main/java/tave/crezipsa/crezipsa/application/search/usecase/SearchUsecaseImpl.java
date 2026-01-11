package tave.crezipsa.crezipsa.application.search.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.search.dto.response.UnifiedSearchResponse;
import tave.crezipsa.crezipsa.application.search.mapper.UnifiedSearchResponseMapper;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchUsecaseImpl implements SearchUsecase {

	private final StoryboardRepositoryPort storyboardRepository;
	private final ChatRoomRepositoryPort chatRoomRepository;
	private final UnifiedSearchResponseMapper mapper;

	@Override
	public UnifiedSearchResponse search(Long userId, String keyword) {
		String q = normalize(keyword);

		if (q.isBlank()) {
			throw new CommonException(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		var storyboards = storyboardRepository.searchByTitle(userId, q);
		var chatRooms = chatRoomRepository.searchByTitle(userId, q);

		var items = mapper.toItems(storyboards, chatRooms);

		return new UnifiedSearchResponse(q, items.isEmpty(), items);
	}

	private String normalize(String keyword) {
		return keyword == null ? "" : keyword.trim();
	}
}
