package tave.crezipsa.crezipsa.application.community.dto.cache;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.global.common.TimeUtils;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
public record CommunitySummaryCacheDto(
	Long communityId,
	CommunityField field,
	String title,
	String contentPreview,
	long likeCount,
	long commentCount,
	LocalDateTime createdAt,
	String thumbnailUrl
) {
	public static CommunitySummaryCacheDto from(Community community, long likeCount, long commentCount) {
		String thumbnail = (community.getImageUrls() != null && !community.getImageUrls().isEmpty())
			? community.getImageUrls().get(0)
			: null;

		return new CommunitySummaryCacheDto(
			community.getCommunityId(),
			community.getField(),
			community.getTitle(),
			TimeUtils.preview(community.getContent()),
			likeCount,
			commentCount,
			community.getCreatedAt(),
			thumbnail
		);
	}

	public CommunitySummaryResponse toResponse() {
		return new CommunitySummaryResponse(
			communityId,
			field,
			title,
			contentPreview,
			likeCount,
			commentCount,
			TimeUtils.convertToRelativeTime(createdAt),
			thumbnailUrl
		);
	}
}
