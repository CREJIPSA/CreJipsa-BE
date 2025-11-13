package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.Optional;
import java.util.UUID;

import tave.crezipsa.crezipsa.domain.community.domain.Like;

public interface LikeRepository {

	Like save(Like like);
	Optional<Like> findById(Long likedId);
	void delete(Like like);
	boolean existsById(Long likedId);

}
