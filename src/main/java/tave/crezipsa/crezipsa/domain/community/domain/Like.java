package tave.crezipsa.crezipsa.domain.community.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.global.common.entity.BaseEntity;

//중간테이블 역할
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@IdClass(LikeId.class) // 복합키 정의 클래스 지정
public class Like extends BaseEntity {

	@Id
	private Long userId;

	@Id
	private Long communityId;

	public static Like of(Long userId, Long communityId) {
		return Like.builder()
			.userId(userId)
			.communityId(communityId)
			.build();
	}

}
