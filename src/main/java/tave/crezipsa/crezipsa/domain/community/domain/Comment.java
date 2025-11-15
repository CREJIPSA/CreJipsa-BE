package tave.crezipsa.crezipsa.domain.community.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;

	private Long communityId;

	private Long userId;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	private Long parentId; //부모댓글 (없으면 null)

	@Builder.Default
	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.LAZY
	)
	@JoinColumn(name = "parentId", referencedColumnName = "commentId")
	private List<Comment> replies = new ArrayList<>();

	public static Comment create(Long communityId, Long userId, String content, Long parentId) {
		if (content == null || content.isEmpty()) {
			throw new CommonException(ErrorCode.INVALID_COMMENT_CONTENT);
		}

		return Comment.builder()
			.communityId(communityId)
			.userId(userId)
			.content(content)
			.parentId(parentId)
			.build();
	}

	public void update (String content) {
		if (content == null || content.isEmpty()) {
			throw new CommonException(ErrorCode.INVALID_COMMENT_CONTENT);
	}
		this.content = content;
	}

	public void deleteCascade() { //대댓글이 달린 댓글 삭제
		replies.clear(); //
	}
}
