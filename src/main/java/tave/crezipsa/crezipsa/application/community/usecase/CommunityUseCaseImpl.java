package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommunityResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityUseCaseImpl implements CommunityUseCase {

	private static final int MAX_KEYWORD_LENGTH = 30;
	private final CommunityRepository communityRepository;
	private final LikeRepository likeRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final CommentUsecase commentUsecase;

	@Override
	public CommunityResponse createCommunity(Long userId, CommunityCreateRequest request) {
		Community community = Community.create(
			request.getTitle(),
			request.getContent(),
			request.getField(),
			request.getImageUrls(),
			userId
		);

		return CommunityResponse.of(communityRepository.save(community));
	}

	@Override
	public CommunityResponse updateCommunity(Long communityId,Long userId, CommunityUpdateRequest communityUpdateRequest) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		if (!Objects.equals(community.getWriterId(), userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}
		community.update(communityUpdateRequest.getTitle(), communityUpdateRequest.getContent(), communityUpdateRequest.getImageUrls());
		return CommunityResponse.of(community);
	}

	@Override
	@Transactional(readOnly = true)
	public CommunityDetailResponse getCommunity(Long communityId, Long userId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		User writerUser = userRepository.findById(community.getWriterId())
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

		long commentCount = commentRepository.countByCommunityId(communityId);
		List<CommentResponse> comments = commentUsecase.getComments(communityId, userId);
		WriterResponse writer = WriterResponse.from(writerUser);
		boolean isWriter = Objects.equals(community.getWriterId(), userId);
		boolean isLiked = likeRepository.findById(new LikeId(userId, communityId))
			.map(Like::isLiked)
			.orElse(false);

		return CommunityDetailResponse.from(community,writer,isWriter,isLiked, commentCount, comments);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommunitySummaryResponse> getAllCommunities() {
		List<Community> communities = communityRepository.findAll();
		Map<Long, Long> commentCounts = loadCommentCounts(communities);

		return communities.stream()
			.map(c -> CommunitySummaryResponse.of(
				c,
				c.getLikeCount(),
				commentCounts.getOrDefault(c.getCommunityId(), 0L)
			))
			.toList();
	}

	@Override
	public void deleteCommunity(Long userId, Long communityId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		if (!community.getWriterId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}
		communityRepository.delete(community);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyCommunityResponse> getMyCommunities(Long userId, CommunityField field, String sort, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<Community> pageResult =
			"popular".equals(sort)
				? communityRepository.findMyCommunitiesPopular(userId,field, pageable)
				: communityRepository.findMyCommunitiesLatest(userId,field, pageable);
		Map<Long, Long> commentCounts = loadCommentCounts(pageResult.getContent());

		return pageResult
			.map(c -> {
				long likeCount = c.getLikeCount();
				long commentCount = commentCounts.getOrDefault(c.getCommunityId(), 0L);
				return MyCommunityResponse.of(c, likeCount, commentCount);
			})
			.getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommunitySummaryResponse> getCommunitiesByField(CommunityField field) {
		List<Community> communities = communityRepository.findByField(field);
		Map<Long, Long> commentCounts = loadCommentCounts(communities);

		return communities.stream()
			.map(c -> CommunitySummaryResponse.of(
				c,
				c.getLikeCount(),
				commentCounts.getOrDefault(c.getCommunityId(), 0L)
			))
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommunitySummaryResponse> searchCommunities(String keyword, CommunityField field, String sort, int page, int size) {
		if (keyword == null || keyword.isBlank()) {
			throw new CommonException(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		String q = keyword.trim();

		if (q.length() > MAX_KEYWORD_LENGTH) {
			throw new CommonException(ErrorCode.SEARCH_KEYWORD_TOO_LONG);
		}


		Pageable pageable = PageRequest.of(page, size);

		Page<Community> pageResult =
			"popular".equals(sort)
				? communityRepository.searchByTitlePopular(q, field, pageable)
				: communityRepository.searchByTitleLatest(q, field, pageable);
		Map<Long, Long> commentCounts = loadCommentCounts(pageResult.getContent());

		return pageResult
			.map(c -> {
				long likeCount = c.getLikeCount();
				long commentCount = commentCounts.getOrDefault(c.getCommunityId(), 0L);
				return CommunitySummaryResponse.of(c, likeCount, commentCount);
			})
			.getContent();
	}

	private Map<Long, Long> loadCommentCounts(List<Community> communities) {
		if (communities == null || communities.isEmpty()) {
			return Collections.emptyMap();
		}
		List<Long> communityIds = communities.stream()
			.map(Community::getCommunityId)
			.toList();
		return commentRepository.countByCommunityIds(communityIds);
	}

}
