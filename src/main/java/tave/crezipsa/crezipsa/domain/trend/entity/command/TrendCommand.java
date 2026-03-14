package tave.crezipsa.crezipsa.domain.trend.entity.command;

public record TrendCommand(
		long keywordId,
		String keyword,
		String category) {

}
