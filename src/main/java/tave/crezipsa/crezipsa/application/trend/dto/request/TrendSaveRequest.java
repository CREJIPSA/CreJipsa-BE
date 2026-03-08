package tave.crezipsa.crezipsa.application.trend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;

public record TrendSaveRequest(
		@Positive long keywordId,
		@NotBlank String keyword,
		@NotBlank String category
) {
	public TrendCommand toCommand() {
		return new TrendCommand(keywordId, keyword, category);
	}
}
