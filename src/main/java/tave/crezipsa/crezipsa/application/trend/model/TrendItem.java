package tave.crezipsa.crezipsa.application.trend.model;

public record TrendItem(
		long id,
		int rank,
		String keyword,
		String platform,
		String category,
		String trendDirection
) {
}
