package tave.crezipsa.crezipsa.infrastructure.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;

public interface LikeJpaRepository extends JpaRepository<Like, LikeId> {
	boolean existsById(LikeId likeId);
	boolean existsByUserIdAndCommunityId(Long userId, Long communityId);
	void deleteByUserIdAndCommunityId(Long userId, Long communityId);
	long countByCommunityId(Long communityId);
	Page<Like> findAllByUserId(Long userId, Pageable pageable);

}
