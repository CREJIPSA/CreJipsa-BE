package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByCommunityId(Long CommunityId);
	List<Comment> findByUserId(Long userId);
	List<Comment> findByParentId(Long parentId);
	long countByCommunityId(Long communityId);
}
