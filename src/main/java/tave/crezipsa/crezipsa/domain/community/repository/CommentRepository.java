package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommentRepository {

	Comment save(Comment comment);
	Optional<Comment> findById(Long id);
	List<Comment> findByCommunityId(Long communityId);
	List<Comment> findByUserId(Long userId);
	List<Comment> findByParentId(Long parentId);
	void delete(Comment comment);
	long countByCommunityId(Long communityId);
	Page<Community> findMyCommentsByCommunityField(Long userId, CommunityField field, Pageable pageable);

}

