package tave.crezipsa.crezipsa.domain.trend.entity.command;

import tave.crezipsa.crezipsa.application.trend.dto.response.request.TrendRequest;

public record TrendCommand(
        long keywordId,
        String keyword,
        String category) {

}
