package tave.crezipsa.crezipsa.infrastructure.storyboard.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "story_board_cut")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoryboardCutJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cut_id")
	private Long cutId;

	@Column(name = "story_board_id", nullable = false)
	private Long storyBoardId;

	@Column(name = "cut_order", nullable = false)
	private int cutOrder;

	@Column(name = "cut_composition", columnDefinition = "TEXT")
	private String cutComposition;

	@Column(name = "script", columnDefinition = "TEXT")
	private String script;

	@Column(name = "caption", columnDefinition = "TEXT")
	private String caption;

	@Column(name = "etc", columnDefinition = "TEXT")
	private String etc;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
}
