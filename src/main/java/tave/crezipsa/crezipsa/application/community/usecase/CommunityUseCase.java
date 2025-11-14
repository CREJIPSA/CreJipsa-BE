package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;


public interface CommunityUseCase {

	CommunityResponse createCommunity(Long userId, CommunityCreateRequest communityCreateRequest);
	CommunityResponse updateCommunity(Long userId,Long communityId, CommunityUpdateRequest communityUpdateRequest);
	CommunityResponse getCommunity(Long communityId);
	List<CommunityResponse> getAllCommunities();
	void deleteCommunity(Long userId,Long communityId);


}
