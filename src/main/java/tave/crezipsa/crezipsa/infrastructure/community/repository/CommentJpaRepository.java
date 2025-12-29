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
select c
from Community c
where c.communityId in (
    select cm.communityId
    from Comment cm
    where cm.userId = :userId
)
order by (
    select max(cm2.createdAt)
    from Comment cm2
    where cm2.communityId = c.communityId
      and cm2.userId = :userId
) desc
""")
	Page<Community> findMyCommentedCommunitiesLatest(
		@Param("userId") Long userId,
		Pageable pageable
	);

	@Query("""
select c
from Community c
where c.communityId in (
    select cm.communityId
    from Comment cm
    where cm.userId = :userId
)
order by c.likeCount desc, c.createdAt desc
""")
	Page<Community> findMyCommentedCommunitiesPopular(
		@Param("userId") Long userId,
		Pageable pageable
	);

}
