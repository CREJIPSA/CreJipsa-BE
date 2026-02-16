package tave.crezipsa.crezipsa.fixture;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public final class CommunityFixture {

	private CommunityFixture() {
	}

	public static Community createCommunity(Long id, Long writerId) {
		Community community = Community.builder()
			.communityId(id)
			.title("테스트 제목")
			.content("테스트 내용입니다. 충분히 긴 내용으로 preview 테스트도 가능합니다.")
			.field(CommunityField.RECOMMEND)
			.imageUrls(List.of("img1.jpg"))
			.writerId(writerId)
			.likeCount(0L)
			.build();
		ReflectionTestUtils.setField(community, "createdAt", LocalDateTime.now());
		return community;
	}

	public static Community createCommunity(Long id) {
		return createCommunity(id, 1L);
	}
}
