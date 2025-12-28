package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;

public interface LikeRepository {

	Like save(Like like);
	Optional<Like> findById(LikeId likedId);
	void delete(Like like);
	boolean existsById(LikeId likedId);
	boolean existsByUserIdAndCommunityId(Long userId, Long communityId);
	void deleteByUserIdAndCommunityId(Long userId, Long communityId);
	long countByCommunityIdAndIsLikedTrue(Long communityId);
	Page<Like> findAllByUserIdAndIsLikedTrue(Long userId, Pageable pageable);
	Page<Community> findMyLikedCommunitiesLatest(Long userId, CommunityField field, Pageable pageable);
	Page<Community> findMyLikedCommunitiesPopular(Long userId, CommunityField field, Pageable pageable);


}
