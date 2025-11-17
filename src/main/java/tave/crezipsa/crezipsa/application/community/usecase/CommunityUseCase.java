package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommunityUseCase {
	// CommunityField 별로 조회도 가능해야하는데 흠흠!!
	CommunityResponse createCommunity(Long userId, CommunityCreateRequest communityCreateRequest);
	CommunityResponse updateCommunity(Long userId,Long communityId, CommunityUpdateRequest communityUpdateRequest);
	CommunityResponse getCommunity(Long communityId);
	List<CommunityResponse> getAllCommunities();
	List<CommunityResponse> getMyCommunities(Long userId);
	List<CommunityResponse> getCommunitiesByField(CommunityField field);
	void deleteCommunity(Long userId,Long communityId);


}
