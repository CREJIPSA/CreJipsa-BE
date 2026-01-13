package tave.crezipsa.crezipsa.domain.trend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class KeywordStoraged {

    private Long keywordStoragedId;
    private Long userId;
    private String keyword;
    private long keywordIdFromAnalytics;
    private String category;
    private LocalDate createdAt;
}
