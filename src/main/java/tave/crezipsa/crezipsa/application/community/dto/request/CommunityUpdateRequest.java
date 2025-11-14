package tave.crezipsa.crezipsa.application.community.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CommunityUpdateRequest {

	private String title;
	private String content;
	private List<String> imageUrls;

}
