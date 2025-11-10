package tave.crezipsa.crezipsa.domain.community.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.global.common.entity.BaseEntity;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Community extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long CommunityId;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	private CommunityField field;

	@Convert(converter = StringListConverter.class)
	private List<String> imageUrls = new ArrayList<>();

	private Long writerId;

	private Long likeCount;

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		if(this.likeCount > 0) {
			this.likeCount--;
		}
	}

	public void update(String title, String content, List<String> imageUrls) {
		if (title != null) this.title = title;
		if (content != null) this.content = content;
		if (imageUrls != null) this.imageUrls = imageUrls;
	}

	public static Community create(String title, String content, CommunityField field, List<String> imageUrls, Long writerId) {
		if( field == null) {
			throw new CommonException(ErrorCode.INVALID_FIELD_TYPE);
		}

		return Community.builder()
			.title(title)
			.content(content)
			.field(field)
			.imageUrls(imageUrls != null ? imageUrls : new ArrayList<>())
			.writerId(writerId)
			.likeCount(0L)
			.build();
	}
}
