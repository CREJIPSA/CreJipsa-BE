package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.application.trend.model.TrendUrl;

public record TrendUrlResponse(
		String title,
		String url,
		int viewCount
) {
	public static TrendUrlResponse from(TrendUrl row) {
		return new TrendUrlResponse(
				row.title(),
				row.url(),
				row.viewCount()
		);
	}
}
