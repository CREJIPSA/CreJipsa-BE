package tave.crezipsa.crezipsa.application.trend.dto.response.request;

import tave.crezipsa.crezipsa.domain.trend.entity.command.TrendCommand;

public record TrendRequest(
        long keywordId,
        String keyword,
        String category
) {
    public TrendCommand from(TrendRequest trendRequest) {
        return new TrendCommand(
                trendRequest.keywordId(),
                trendRequest.keyword(),
                trendRequest.category());
    }
}
