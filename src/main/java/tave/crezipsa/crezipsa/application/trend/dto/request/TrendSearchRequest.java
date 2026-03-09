package tave.crezipsa.crezipsa.application.trend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TrendSearchRequest {

	@NotBlank(message = "검색어는 필수입니다.")
	@Size(max = 20, message = "검색어는 20자 이하만 가능합니다.")
	private String trend;

	public TrendSearchRequest(String trend) {
		this.trend = trend;
	}
}
