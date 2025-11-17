package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
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
		return commentJpaRepository.findByCommentId(communityId);
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

}
