package tave.crezipsa.crezipsa.application.trend.model;

public record TrendDetail(
		long id,
		String platform,
		String category,
		int overallRank,
		int categoryRank,
		String keyword,
		double viralityScore
) {
}
