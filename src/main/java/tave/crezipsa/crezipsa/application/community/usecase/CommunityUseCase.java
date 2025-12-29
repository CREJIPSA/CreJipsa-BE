package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommunityUseCase {

	CommunityResponse createCommunity(Long userId, CommunityCreateRequest communityCreateRequest);
	CommunityResponse updateCommunity(Long userId,Long communityId, CommunityUpdateRequest communityUpdateRequest);
	CommunityDetailResponse getCommunity(Long communityId);
	List<CommunitySummaryResponse> getAllCommunities();
	List<MyCommunityResponse> getMyCommunities(Long userId,CommunityField field, String sort, int page, int size);
	List<CommunitySummaryResponse> getCommunitiesByField(CommunityField field);
	void deleteCommunity(Long userId,Long communityId);


}
