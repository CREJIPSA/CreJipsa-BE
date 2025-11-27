package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.*;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
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

	@Override
	public CommunityDetailResponse createCommunity(Long userId, CommunityCreateRequest communityCreateRequest) {
		Community community = Community.builder()
			.title(communityCreateRequest.getTitle())
			.content(communityCreateRequest.getContent())
			.imageUrls(communityCreateRequest.getImageUrls())
			.field(communityCreateRequest.getField())
			.writerId(userId)
			.likeCount(0L)
			.build();

		return CommunityDetailResponse.from(communityRepository.save(community));
	}

	@Override
	public CommunityDetailResponse updateCommunity(Long userId,Long communityId, CommunityUpdateRequest communityUpdateRequest) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		if (!Objects.equals(community.getWriterId(), userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}
		community.update(communityUpdateRequest.getTitle(), communityUpdateRequest.getContent(), communityUpdateRequest.getImageUrls());
		return CommunityDetailResponse.from(community);
	}

	@Override
	public CommunityDetailResponse getCommunity(Long communityId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		return CommunityDetailResponse.from(community);
	}

	@Override
	public List<CommunityDetailResponse> getAllCommunities() {
		return communityRepository.findAll().stream()
			.map(CommunityDetailResponse::from)
			.collect(Collectors.toList());
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
	public List<CommunityDetailResponse> getCommunitiesByField(CommunityField field) {
		return communityRepository.findByField(field)
			.stream()
			.map(CommunityDetailResponse::from)
			.toList();
	}

}
