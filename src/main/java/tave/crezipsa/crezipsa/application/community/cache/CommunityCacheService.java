package tave.crezipsa.crezipsa.application.community.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.cache.CommentCacheDto;
import tave.crezipsa.crezipsa.application.community.dto.cache.CommunityDetailCacheDto;
import tave.crezipsa.crezipsa.application.community.dto.cache.CommunitySummaryCacheDto;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
public class CommunityCacheService {

	private final CommunityRepository communityRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	@Cacheable(value = "community-list", key = "'all'")
	@Transactional(readOnly = true)
	public List<CommunitySummaryCacheDto> getAllCommunitiesCache() {
		List<Community> communities = communityRepository.findAll();
		Map<Long, Long> commentCounts = loadCommentCounts(communities);

		return communities.stream()
			.map(c -> CommunitySummaryCacheDto.from(
				c,
				c.getLikeCount(),
				commentCounts.getOrDefault(c.getCommunityId(), 0L)
			))
			.collect(Collectors.toList());
	}

	@Cacheable(value = "community-list-by-field", key = "#field.name()")
	@Transactional(readOnly = true)
	public List<CommunitySummaryCacheDto> getCommunitiesByFieldCache(CommunityField field) {
		List<Community> communities = communityRepository.findByField(field);
		Map<Long, Long> commentCounts = loadCommentCounts(communities);

		return communities.stream()
			.map(c -> CommunitySummaryCacheDto.from(
				c,
				c.getLikeCount(),
				commentCounts.getOrDefault(c.getCommunityId(), 0L)
			))
			.collect(Collectors.toList());
	}

	@Cacheable(value = "community-detail", key = "#communityId")
	@Transactional(readOnly = true)
	public CommunityDetailCacheDto getCommunityDetailCache(Long communityId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		User writerUser = userRepository.findById(community.getWriterId())
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

		long commentCount = commentRepository.countByCommunityId(communityId);

		return CommunityDetailCacheDto.from(community, WriterResponse.from(writerUser), commentCount);
	}

	@Cacheable(value = "comment-list", key = "#communityId")
	@Transactional(readOnly = true)
	public List<CommentCacheDto> getCommentsCache(Long communityId) {
		return buildCommentCacheDtos(communityId);
	}

	// ===== 캐시 무효화 =====

	@Caching(evict = {
		@CacheEvict(value = "community-list", allEntries = true),
		@CacheEvict(value = "community-list-by-field", allEntries = true)
	})
	public void evictCommunityLists() {}

	@Caching(evict = {
		@CacheEvict(value = "community-list", allEntries = true),
		@CacheEvict(value = "community-list-by-field", allEntries = true),
		@CacheEvict(value = "community-detail", key = "#communityId")
	})
	public void evictCommunityAll(Long communityId) {}

	@Caching(evict = {
		@CacheEvict(value = "community-list", allEntries = true),
		@CacheEvict(value = "community-list-by-field", allEntries = true),
		@CacheEvict(value = "community-detail", key = "#communityId"),
		@CacheEvict(value = "comment-list", key = "#communityId")
	})
	public void evictCommunityAndComments(Long communityId) {}

	@Caching(evict = {
		@CacheEvict(value = "community-detail", key = "#communityId"),
		@CacheEvict(value = "comment-list", key = "#communityId")
	})
	public void evictCommentsAndDetail(Long communityId) {}

	// ===== 내부 헬퍼 =====

	private List<CommentCacheDto> buildCommentCacheDtos(Long communityId) {
		List<Comment> allComments = commentRepository.findByCommunityId(communityId);
		if (allComments.isEmpty()) {
			return new ArrayList<>();
		}

		Set<Long> writerIds = allComments.stream()
			.map(Comment::getUserId)
			.collect(Collectors.toSet());

		Map<Long, User> writerMap = userRepository.findAllById(writerIds).stream()
			.collect(Collectors.toMap(User::getUserId, u -> u));

		Map<Long, List<Comment>> childrenByParentId = allComments.stream()
			.filter(c -> c.getParentId() != null)
			.collect(Collectors.groupingBy(Comment::getParentId));

		return allComments.stream()
			.filter(c -> c.getParentId() == null)
			.sorted(Comparator.comparing(Comment::getCreatedAt))
			.map(root -> toCommentCacheTree(root, childrenByParentId, writerMap))
			.collect(Collectors.toList());
	}

	private CommentCacheDto toCommentCacheTree(
		Comment comment,
		Map<Long, List<Comment>> childrenByParentId,
		Map<Long, User> writerMap
	) {
		User writer = writerMap.get(comment.getUserId());
		if (writer == null) {
			throw new CommonException(ErrorCode.USER_NOT_FOUND);
		}

		List<CommentCacheDto> replies = childrenByParentId
			.getOrDefault(comment.getCommentId(), Collections.emptyList())
			.stream()
			.sorted(Comparator.comparing(Comment::getCreatedAt))
			.map(child -> toCommentCacheTree(child, childrenByParentId, writerMap))
			.collect(Collectors.toList());

		return new CommentCacheDto(
			comment.getCommentId(),
			comment.getCommunityId(),
			comment.getParentId(),
			comment.getUserId(),
			WriterResponse.from(writer),
			comment.isDeleted(),
			comment.getContent(),
			comment.getCreatedAt(),
			replies
		);
	}

	private Map<Long, Long> loadCommentCounts(List<Community> communities) {
		if (communities == null || communities.isEmpty()) {
			return Collections.emptyMap();
		}
		List<Long> communityIds = communities.stream()
			.map(Community::getCommunityId)
			.collect(Collectors.toList());
		return commentRepository.countByCommunityIds(communityIds);
	}
}
