package tave.crezipsa.crezipsa.application.community.dto.response;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record CommunityResponse(
	Long communityId,
	CommunityField field,
	String title,
	String content) {

	public static CommunityResponse of(Community community) {
		return new CommunityResponse(
			community.getCommunityId(),
			community.getField(),
			community.getTitle(),
			community.getContent()
		);
	}

}
