package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.Optional;
import java.util.UUID;

import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;

public interface LikeRepository {

	Like save(Like like);
	Optional<Like> findById(LikeId likedId);
	void delete(Like like);
	boolean existsById(LikeId likedId);

}
