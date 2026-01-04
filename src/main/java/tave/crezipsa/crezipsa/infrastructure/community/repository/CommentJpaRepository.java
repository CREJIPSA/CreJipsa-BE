package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByCommunityId(Long CommunityId);
	List<Comment> findByUserId(Long userId);
	List<Comment> findByParentId(Long parentId);
	long countByCommunityId(Long communityId);
	@Query("""
select cm
from Comment cm
join Community c on c.communityId = cm.communityId
where cm.userId = :userId
  and (:field is null or c.field = :field)
order by cm.createdAt desc
""")
	Page<Community> findMyCommentsByCommunityField(
		@Param("userId") Long userId,
		@Param("field") CommunityField field,
		Pageable pageable
	);


}
