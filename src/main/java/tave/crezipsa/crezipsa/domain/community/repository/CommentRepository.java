package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;

public interface CommentRepository {

	Comment save(Comment comment);
	Optional<Comment> findById(Long id);
	List<Comment> findByCommunityId(Long communityId);
	List<Comment> findByUserId(Long userId);
	List<Comment> findByParentId(Long parentId);
	void delete(Comment comment);
	long countByCommunityId(Long communityId);
}

