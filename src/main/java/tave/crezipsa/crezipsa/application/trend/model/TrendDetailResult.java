package tave.crezipsa.crezipsa.application.trend.model;

import java.util.List;

public record TrendDetailResult(
		TrendDetail detail,
		List<TrendUrl> urls
) {
}
