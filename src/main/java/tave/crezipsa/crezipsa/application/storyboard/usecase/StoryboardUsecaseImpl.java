package tave.crezipsa.crezipsa.application.storyboard.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardStructuredResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardStructurerPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional
public class StoryboardUsecaseImpl implements StoryboardUsecase {

	private final StoryboardRepositoryPort storyboardRepository;
	private final ChatMessageRepositoryPort chatMessageRepository;
	private final StoryboardStructurerPort storyboardStructurerPort;

	@Override
	public StoryboardStructuredResponse createFromChatMessage(
		Long userId,
		Long chatMessageId,
		String title
	) {
		ChatMessage message = chatMessageRepository.findById(chatMessageId);

		if (message == null) {
			throw new CommonException(ErrorCode.CHAT_NOT_FOUND);
		}

		if (message.getSenderType() != ChatMessage.SenderType.AI) {
			throw new CommonException(ErrorCode.INVALID_SENDER_TYPE);
		}

		StoryboardStructuredResponse structured =
			storyboardStructurerPort.structure(message.getContent());

		Storyboard storyboard = Storyboard.create(
			userId,
			(title == null || title.isBlank()) ? "AI 스토리보드" : title,
			structured.cutSummary(),
			structured.script(),
			structured.caption(),
			structured.time()
		);

		storyboardRepository.save(storyboard);

		return structured;
	}

	@Override
	@Transactional(readOnly = true)
	public StoryboardStructuredResponse get(Long userId, Long storyboardId) {
		Storyboard storyboard = storyboardRepository.findById(storyboardId);

		if (storyboard == null || !storyboard.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		return StoryboardStructuredResponse.from(storyboard);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoryboardSummaryResponse> getMyList(Long userId) {
		return storyboardRepository.findByUserId(userId)
			.stream()
			.map(sb -> new StoryboardSummaryResponse(
				sb.getId(),
				sb.getTitle(),
				sb.getCreatedAt()
			))
			.toList();
	}

	@Override
	public StoryboardStructuredResponse update(
		Long userId,
		Long storyboardId,
		UpdateStoryboardRequest request
	) {
		Storyboard storyboard = storyboardRepository.findById(storyboardId);

		if (storyboard == null || !storyboard.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		Storyboard updated = Storyboard.builder()
			.id(storyboard.getId())
			.userId(storyboard.getUserId())
			.title(request.title() != null ? request.title() : storyboard.getTitle())
			.cutSummary(request.cutSummary() != null ? request.cutSummary() : storyboard.getCutSummary())
			.script(request.script() != null ? request.script() : storyboard.getScript())
			.caption(request.caption() != null ? request.caption() : storyboard.getCaption())
			.time(request.time() != null ? request.time() : storyboard.getTime())
			.createdAt(storyboard.getCreatedAt())
			.build();

		storyboardRepository.save(updated);

		return new StoryboardStructuredResponse(
			updated.getCutSummary(),
			updated.getScript(),
			updated.getCaption(),
			updated.getTime()
		);
	}


	@Override
	public void delete(Long userId, Long storyboardId) {
		Storyboard storyboard = storyboardRepository.findById(storyboardId);

		if (storyboard == null || !storyboard.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		storyboardRepository.deleteById(storyboardId);
	}
}
