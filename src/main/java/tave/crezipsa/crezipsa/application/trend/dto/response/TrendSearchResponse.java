package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.infrastructure.trend.TrendWithUrls;

import java.util.List;

public record TrendSearchResponse(
        List<TrendListResponse> trends,
        List<TrendUrlResponse> videos
) {
    public static TrendSearchResponse from(TrendWithUrls result) {
        return new TrendSearchResponse(
                result.rows().stream().map(TrendListResponse::from).toList(),
                result.urls().stream().map(TrendUrlResponse::from).toList()
        );
    }
}
