package tave.crezipsa.crezipsa.application.storyboard.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.CreateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardCutRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardTitleRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardCutResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardEditorResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.entity.StoryboardCut;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardCutRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional
public class StoryboardUsecaseImpl implements StoryboardUsecase {

	private final StoryboardRepositoryPort storyboardRepository;
	private final StoryboardCutRepositoryPort storyboardCutRepository;
	private final ChatMessageRepositoryPort chatMessageRepository;

	@Override
	public StoryboardEditorResponse create(Long userId, CreateStoryboardRequest request) {
		Long chatMessageId = request.chatMessageId();

		// 채팅에서 편집 진입한 경우만 검증
		if (chatMessageId != null) {
			ChatMessage message = chatMessageRepository.findById(chatMessageId);
			if (message == null) {
				throw new CommonException(ErrorCode.CHAT_NOT_FOUND);
			}
			if (message.getSenderType() != ChatMessage.SenderType.AI) {
				throw new CommonException(ErrorCode.INVALID_SENDER_TYPE);
			}
		}

		// 문서 생성 (스토리보드)
		Storyboard saved = storyboardRepository.save(
			Storyboard.create(userId, request.title(), chatMessageId)
		);

		// 기본 컷 1개 자동 생성
		int order = storyboardCutRepository.findNextOrder(saved.getId());
		StoryboardCut firstCut = storyboardCutRepository.save(
			StoryboardCut.create(saved.getId(), order)
		);

		return new StoryboardEditorResponse(
			saved.getId(),
			saved.getTitle(),
			saved.getSourceChatMessageId(),
			saved.getCreatedAt(),
			List.of(toCutResponse(firstCut))
		);
	}

	@Override
	@Transactional(readOnly = true)
	public StoryboardEditorResponse get(Long userId, Long storyboardId) {
		Storyboard sb = storyboardRepository.findById(storyboardId);
		if (sb == null || !sb.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		List<StoryboardCutResponse> cuts = storyboardCutRepository.findByStoryboardId(storyboardId)
			.stream()
			.map(this::toCutResponse)
			.toList();

		return new StoryboardEditorResponse(
			sb.getId(),
			sb.getTitle(),
			sb.getSourceChatMessageId(),
			sb.getCreatedAt(),
			cuts
		);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoryboardSummaryResponse> getMyList(Long userId) {
		return storyboardRepository.findByUserId(userId)
			.stream()
			.map(sb -> new StoryboardSummaryResponse(sb.getId(), sb.getTitle(), sb.getCreatedAt()))
			.toList();
	}

	@Override
	public void updateTitle(Long userId, Long storyboardId, UpdateStoryboardTitleRequest request) {
		Storyboard sb = getOwnedStoryboardOrThrow(userId, storyboardId);

		String title = request.title();
		if (title == null || title.isBlank()) {
			throw new CommonException(ErrorCode.INVALID_INPUT_VALUE);
		}

		storyboardRepository.save(sb.withTitle(title));
	}

	@Override
	public StoryboardCutResponse addCut(Long userId, Long storyboardId) {
		getOwnedStoryboardOrThrow(userId, storyboardId);

		int order = storyboardCutRepository.findNextOrder(storyboardId);
		StoryboardCut cut = storyboardCutRepository.save(
			StoryboardCut.create(storyboardId, order)
		);

		return toCutResponse(cut);
	}

	@Override
	public StoryboardCutResponse updateCut(Long userId, Long cutId, UpdateStoryboardCutRequest request) {
		StoryboardCut cut = getCutOrThrow(cutId);
		getOwnedStoryboardByCutOrThrow(userId, cut);

		StoryboardCut updated = StoryboardCut.builder()
			.id(cut.getId())
			.storyboardId(cut.getStoryboardId())
			.order(cut.getOrder())
			.cutComposition(request.cutComposition() != null ? request.cutComposition() : cut.getCutComposition())
			.script(request.script() != null ? request.script() : cut.getScript())
			.caption(request.caption() != null ? request.caption() : cut.getCaption())
			.etc(request.etc() != null ? request.etc() : cut.getEtc())
			.build();

		return toCutResponse(storyboardCutRepository.save(updated));

	}

	@Override
	public void deleteCut(Long userId, Long cutId) {

		StoryboardCut cut = storyboardCutRepository.findById(cutId);
		if (cut == null) {
			throw new CommonException(ErrorCode.STORYBOARD_CUT_NOT_FOUND);
		}

		Storyboard sb = storyboardRepository.findById(cut.getStoryboardId());
		if (sb == null || !sb.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		storyboardCutRepository.deleteById(cutId);
	}

	@Override
	public void deleteStoryboard(Long userId, Long storyboardId) {

		Storyboard sb = storyboardRepository.findById(storyboardId);
		if (sb == null || !sb.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		storyboardRepository.deleteById(storyboardId);
	}

	private StoryboardCutResponse toCutResponse(StoryboardCut c) {
		return new StoryboardCutResponse(
			c.getId(),
			c.getOrder(),
			c.getCutComposition(),
			c.getScript(),
			c.getCaption(),
			c.getEtc()
		);
	}
	private Storyboard getOwnedStoryboardOrThrow(Long userId, Long storyboardId) {
		Storyboard sb = storyboardRepository.findById(storyboardId);
		if (sb == null || !sb.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}
		return sb;
	}

	private StoryboardCut getCutOrThrow(Long cutId) {
		StoryboardCut cut = storyboardCutRepository.findById(cutId);
		if (cut == null) {
			throw new CommonException(ErrorCode.STORYBOARD_CUT_NOT_FOUND);
		}
		return cut;
	}

	private Storyboard getOwnedStoryboardByCutOrThrow(Long userId, StoryboardCut cut) {
		Storyboard sb = storyboardRepository.findById(cut.getStoryboardId());
		if (sb == null || !sb.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.STORYBOARD_NOT_FOUND);
		}
		return sb;
	}

}
