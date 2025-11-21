package tave.crezipsa.crezipsa.application.community.usecase;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.response.MyLikedCommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeUseCaseImpl  implements LikeUseCase {

	private final LikeRepository likeRepository;
	private final CommunityRepository communityRepository;
	private final CommentRepository commentRepository;

	@Override
	public void like(Long userId, Long communityId) {
		LikeId likeId = new LikeId(userId, communityId);

		if (likeRepository.existsById(likeId)) {
			throw new CommonException(ErrorCode.ALREADY_LIKED);
		}
	}

	@Override
	public void unlike(Long userId, Long communityId) {
		LikeId likeId = new LikeId(userId, communityId);

		if (!likeRepository.existsById(likeId)) {
			throw new CommonException(ErrorCode.NOT_LIKED);
		}

		likeRepository.delete(Like.of(userId, communityId));
	}

	@Override
	public Long getLikeCount(Long communityId) {
		return likeRepository.countByCommunityId(communityId);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<MyLikedCommunityResponse> getMyLikedCommunities(Long userId, Pageable pageable) {
		return likeRepository.findAllByUserId(userId, pageable)
			.map(like -> {
				Community community = communityRepository.findById(like.getCommunityId())
					.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));
				long likeCount = likeRepository.countByCommunityId(like.getCommunityId());
				long commentCount = commentRepository.countByCommunityId(community.getCommunityId());

				return MyLikedCommunityResponse.of(community, likeCount, commentCount);
			});
	}
}
