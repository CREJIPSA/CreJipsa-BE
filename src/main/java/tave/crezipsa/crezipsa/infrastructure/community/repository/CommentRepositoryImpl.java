package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

	private final CommentJpaRepository commentJpaRepository;

	@Override
	public Comment save(Comment comment) {
		return commentJpaRepository.save(comment);
	}

	@Override
	public Optional<Comment> findById(Long id) {
		return commentJpaRepository.findById(id);
	}

	@Override
	public List<Comment> findByCommunityId(Long communityId) {
		return commentJpaRepository.findByCommunityId(communityId);
	}

	@Override
	public void delete(Comment comment) {
		commentJpaRepository.delete(comment);
	}

	@Override
	public List<Comment> findByUserId(Long userId) {
		return commentJpaRepository.findByUserId(userId);
	}

	@Override
	public List<Comment> findByParentId(Long parentId) {
		return commentJpaRepository.findByParentId(parentId);
	}

	@Override
	public long countByCommunityId(Long communityId) {
		return commentJpaRepository.countByCommunityId(communityId);
	}

	@Override
	public Map<Long, Long> countByCommunityIds(List<Long> communityIds) {
		if (communityIds == null || communityIds.isEmpty()) {
			return Collections.emptyMap();
		}
		return commentJpaRepository.countByCommunityIds(communityIds).stream()
			.collect(Collectors.toMap(
				CommentJpaRepository.CommunityCommentCount::getCommunityId,
				CommentJpaRepository.CommunityCommentCount::getCount
			));
	}

	@Override
	public Page<Community> findMyCommentsByCommunityField(Long userId, CommunityField field, Pageable pageable) {
		return commentJpaRepository.findMyCommentsByCommunityField(userId, field, pageable);
	}
}
