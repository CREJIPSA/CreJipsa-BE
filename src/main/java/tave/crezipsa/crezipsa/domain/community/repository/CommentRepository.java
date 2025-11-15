package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;

public interface CommentRepository {

	Comment save(Comment comment);
	Optional<Comment> findById(Long id);
	List<Comment> findByCommunityId(Long communityId);
	void delete(Comment comment);

}
