package tave.crezipsa.crezipsa.application.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

@Getter
@AllArgsConstructor
@Builder
public class CommunityResponse {

	private Long communityId;
	private String title;
	private String content;
	private CommunityField field;
	private List<String> imageUrls;
	private Long writerId;
	private Long likeCount;
	private LocalDateTime createdAt;

	public static CommunityResponse from(Community community) {
		return CommunityResponse.builder()
			.communityId(community.getCommunityId())
			.title(community.getTitle())
			.content(community.getContent())
			.field(community.getField())
			.imageUrls(community.getImageUrls())
			.writerId(community.getWriterId())
			.likeCount(community.getLikeCount())
			.createdAt(community.getCreatedAt())
			.build();
	}
}
