package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.infrastructure.trend.TrendUrlRow;

public record TrendUrlResponse(
		String title,
		String url,
		int viewCount
) {
	public static TrendUrlResponse from(TrendUrlRow row) {
		return new TrendUrlResponse(
				row.title(),
				row.url(),
				row.viewCount()
		);
	}
}
