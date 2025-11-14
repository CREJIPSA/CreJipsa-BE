package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.*;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityUseCaseImpl implements CommunityUseCase {


	private final CommunityRepository communityRepository;

	@Override
	public CommunityResponse createCommunity(Long userId, CommunityCreateRequest communityCreateRequest) {
		Community community = Community.builder()
			.title(communityCreateRequest.getTitle())
			.content(communityCreateRequest.getContent())
			.imageUrls(communityCreateRequest.getImageUrls())
			.field(communityCreateRequest.getField())
			.writerId(userId)
			.likeCount(0L)
			.build();

		return CommunityResponse.from(communityRepository.save(community));
	}

	@Override
	public CommunityResponse updateCommunity(Long userId,Long communityId, CommunityUpdateRequest communityUpdateRequest) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		if (!community.getWriterId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}
		community.update(communityUpdateRequest.getTitle(), communityUpdateRequest.getContent(), communityUpdateRequest.getImageUrls());
		return CommunityResponse.from(community);
	}

	@Override
	public CommunityResponse getCommunity(Long communityId) {
		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		return CommunityResponse.from(community);
	}

	@Override
	public List<CommunityResponse> getAllCommunities() {
		return communityRepository.findAll().stream()
			.map(CommunityResponse::from)
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

}
