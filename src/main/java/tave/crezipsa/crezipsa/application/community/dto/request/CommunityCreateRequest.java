package tave.crezipsa.crezipsa.application.community.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

@Getter
@NoArgsConstructor
public class CommunityCreateRequest {

	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	private String content;

	private List<String> imageUrls;

	@NotBlank(message = "글 타입을 지정해주세요")
	private CommunityField field;

}
