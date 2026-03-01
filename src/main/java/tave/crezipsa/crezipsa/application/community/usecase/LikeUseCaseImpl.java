package tave.crezipsa.crezipsa.application.community.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.cache.CommunityCacheService;
import tave.crezipsa.crezipsa.application.community.dto.response.MyLikedCommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.global.common.TransactionUtils;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeUseCaseImpl  implements LikeUseCase {

	private final LikeRepository likeRepository;
	private final CommunityRepository communityRepository;
	private final CommentRepository commentRepository;
	private final CommunityCacheService communityCacheService;

	@Override
	public void like(Long userId, Long communityId) {
		LikeId likeId = new LikeId(userId, communityId);

		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		Like like = likeRepository.findById(likeId).orElse(null);

		if (like == null) {
			likeRepository.save(Like.of(userId, communityId));
			community.increaseLikeCount();
		} else if (like.isLiked()) {
			like.unlike();
			community.decreaseLikeCount();
		} else {
			like.like();
			community.increaseLikeCount();
		}
		TransactionUtils.afterCommit(() -> communityCacheService.evictCommunityAll(communityId));
	}

	@Override
	public void unlike(Long userId, Long communityId) {
		LikeId likeId = new LikeId(userId, communityId);

		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		Like like = likeRepository.findById(likeId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_LIKED));

		if (!like.isLiked()) {
			throw new CommonException(ErrorCode.NOT_LIKED);
		}
		like.unlike();
		community.decreaseLikeCount();
		TransactionUtils.afterCommit(() -> communityCacheService.evictCommunityAll(communityId));
	}

	@Override
	public long getLikeCount(Long communityId) {
		return likeRepository.countByCommunityIdAndIsLikedTrue(communityId);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<MyLikedCommunityResponse> getMyLikedCommunities(Long userId, CommunityField field, String sort, Pageable pageable) {
		Page<Community> page =
			"popular".equals(sort)
				? likeRepository.findMyLikedCommunitiesPopular(userId, field, pageable)
				: likeRepository.findMyLikedCommunitiesLatest(userId, field, pageable);

		return page.map(community -> {
			long commentCount =
				commentRepository.countByCommunityId(community.getCommunityId());

			return MyLikedCommunityResponse.of(
				community,
				community.getLikeCount(),
				commentCount
			);
		});
	}

}
