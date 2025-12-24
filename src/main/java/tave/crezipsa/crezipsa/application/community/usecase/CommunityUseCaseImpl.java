package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.*;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityUseCaseImpl implements CommunityUseCase {


	private final CommunityRepository communityRepository;
	private final LikeRepository likeRepository;
	private final CommentRepository commentRepository;
	private final CommentUsecase commentUsecase;

	@Override
	public CommunityResponse createCommunity(Long userId, CommunityCreateRequest request) {
		Community community = Community.create(
			request.getTitle(),
			request.getContent(),
			request.getField(),
			request.getImageUrls(),
			userId
		);

		return CommunityResponse.of(communityRepository.save(community));
	}

	@Override
	public CommunityResponse updateCommunity(Long communityId,Long userId, CommunityUpdateRequest communityUpdateRequest) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		if (!Objects.equals(community.getWriterId(), userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}
		community.update(communityUpdateRequest.getTitle(), communityUpdateRequest.getContent(), communityUpdateRequest.getImageUrls());
		return CommunityResponse.of(community);
	}

	@Override
	public CommunityDetailResponse getCommunity(Long communityId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		long commentCount = commentRepository.countByCommunityId(communityId);
		List<CommentResponse> comments = commentUsecase.getComments(communityId);


		return CommunityDetailResponse.from(community, commentCount, comments);
	}

	@Override
	public List<CommunitySummaryResponse> getAllCommunities() {
		return communityRepository.findAll().stream()
			.map(c -> {
				long likeCount = likeRepository.countByCommunityIdAndIsLikedTrue(c.getCommunityId());
				long commentCount = commentRepository.countByCommunityId(c.getCommunityId());
				return CommunitySummaryResponse.of(c, likeCount, commentCount);
			})
			.toList();
	}

	@Override
	public void deleteCommunity(Long userId, Long communityId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		if (!community.getWriterId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}
		communityRepository.delete(community);
	}

	@Override
	public List<MyCommunityResponse> getMyCommunities(Long userId) {
		return communityRepository.findByWriterId(userId)
			.stream()
			.map(c -> {
				long likeCount = likeRepository.countByCommunityIdAndIsLikedTrue(c.getCommunityId());
				long commentCount = commentRepository.countByCommunityId(c.getCommunityId());
				return MyCommunityResponse.of(c, likeCount, commentCount);
			})
			.toList();
	}

	@Override
	public List<CommunitySummaryResponse> getCommunitiesByField(CommunityField field) {
		return communityRepository.findByField(field)
			.stream()
			.map(c -> {
				long likeCount = likeRepository.countByCommunityIdAndIsLikedTrue(c.getCommunityId());
				long commentCount = commentRepository.countByCommunityId(c.getCommunityId());
				return CommunitySummaryResponse.of(c, likeCount, commentCount);
			})
			.toList();
	}

}
