package tave.crezipsa.crezipsa.application.community.usecase;

import org.springframework.data.domain.Pageable;

public interface LikeUseCase {

	void like(Long userId, Long communityId);
	void unlike(Long userId, Long communityId);
	Long getLikeCount(Long community);
	Slice<MyLikedCommunityResponse> getMyLikedCommunities(Long userId, Pageable pageable);

}
