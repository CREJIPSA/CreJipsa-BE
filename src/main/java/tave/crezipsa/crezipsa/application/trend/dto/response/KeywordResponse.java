package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.domain.trend.entity.KeywordStoraged;

import java.time.LocalDate;

public record KeywordResponse(
        Long keywordStoragedId,
        String keyword,
        String category,
        LocalDate createdAt
) {
    public static KeywordResponse from(KeywordStoraged entity) {
        return new KeywordResponse(
                entity.getKeywordStoragedId(),
                entity.getKeyword(),
                entity.getCategory(),
                entity.getCreatedAt()
        );
    }
}
