package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByCommunityId(Long CommunityId);
	List<Comment> findByUserId(Long userId);
	List<Comment> findByParentId(Long parentId);
	long countByCommunityId(Long communityId);

	@Query("""
select distinct c
from Comment cm
join Community c on c.communityId = cm.communityId
where cm.userId = :userId
order by cm.createdAt desc
""")
	Page<Community> findMyCommentedCommunitiesLatest(
		@Param("userId") Long userId,
		Pageable pageable
	);

	@Query("""
select distinct c
from Comment cm
join Community c on c.communityId = cm.communityId
where cm.userId = :userId
order by c.likeCount desc, cm.createdAt desc
""")
	Page<Community> findMyCommentedCommunitiesPopular(
		@Param("userId") Long userId,
		Pageable pageable
	);

}
