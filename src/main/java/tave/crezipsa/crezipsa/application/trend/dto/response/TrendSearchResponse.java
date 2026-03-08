package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.infrastructure.trend.TrendWithUrls;

import java.util.List;

public record TrendSearchResponse(
		List<TrendResponse> trends,
		List<TrendUrlResponse> videos
) {
	public static TrendSearchResponse from(TrendWithUrls result) {
		if (result == null) {
			return new TrendSearchResponse(List.of(), List.of());
		}

		List<TrendResponse> trends = result.rows() == null
				? List.of()
				: result.rows().stream().map(TrendResponse::from).toList();
		List<TrendUrlResponse> videos = result.urls() == null
				? List.of()
				: result.urls().stream().map(TrendUrlResponse::from).toList();

		return new TrendSearchResponse(
				trends,
				videos
		);
	}
}
