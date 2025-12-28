package tave.crezipsa.crezipsa.infrastructure.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;

public interface LikeJpaRepository extends JpaRepository<Like, LikeId> {
	boolean existsById(LikeId likeId);
	boolean existsByUserIdAndCommunityId(Long userId, Long communityId);
	void deleteByUserIdAndCommunityId(Long userId, Long communityId);
	long countByCommunityIdAndIsLikedTrue(Long communityId);
	Page<Like> findAllByUserIdAndIsLikedTrue(Long userId, Pageable pageable);

	//내가 좋아요 누른 글 정렬기준 JPQL 메서드 추가
	@Query("""
	select c
	from Like l
	join Community c on c.communityId = l.communityId
	where l.userId = :userId
	  and l.isLiked = true
	  and (:field is null or c.field = :field)
	order by c.createdAt desc
""")
	Page<Community> findMyLikedCommunitiesLatest(
		@Param("userId")Long userId,
		@Param("field")CommunityField field,
		Pageable pageable
	);

	@Query("""
	select c
	from Like l
	join Community c on c.communityId = l.communityId
	where l.userId = :userId
	  and l.isLiked = true
	  and (:field is null or c.field = :field)
	order by c.likeCount desc, c.createdAt desc
""")
	Page<Community> findMyLikedCommunitiesPopular(
		@Param("userId")Long userId,
		@Param("field")CommunityField field,
		Pageable pageable
	);



}
