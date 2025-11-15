package tave.crezipsa.crezipsa.infrastructure.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;

public interface LikeJpaRepository extends JpaRepository<Like, LikeId> {
	boolean existsById(LikeId likeId);
}
