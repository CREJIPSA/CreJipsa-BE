package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.application.trend.model.TrendSearchResult;

import java.util.List;

public record TrendSearchResponse(
		List<TrendResponse> trends,
		List<TrendUrlResponse> videos
) {
	public static TrendSearchResponse from(TrendSearchResult result) {
		if (result == null) {
			return new TrendSearchResponse(List.of(), List.of());
		}

		List<TrendResponse> trends = result.trends() == null
				? List.of()
				: result.trends().stream().map(TrendResponse::from).toList();
		List<TrendUrlResponse> videos = result.videos() == null
				? List.of()
				: result.videos().stream().map(TrendUrlResponse::from).toList();

		return new TrendSearchResponse(
				trends,
				videos
		);
	}
}
