package tave.crezipsa.crezipsa.application.trend.model;

import java.util.List;

public record TrendSearchResult(
		List<TrendItem> trends,
		List<TrendUrl> videos
) {
}
