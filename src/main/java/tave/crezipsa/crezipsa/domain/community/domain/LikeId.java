package tave.crezipsa.crezipsa.domain.community.domain;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class LikeId implements Serializable {

	private Long userId;
	private Long communityId;

	public LikeId() {}

	public LikeId(Long userId, Long communityId) {
		this.userId = userId;
		this.communityId = communityId;
	}
}
