package tave.crezipsa.crezipsa.application.trend.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;

public record UserHistoryResponse (
        long historyId,
        String history
){
    public static UserHistoryResponse from(UserHistory h) {
        return new UserHistoryResponse(h.getHistoryId(), h.getHistoryContent());
    }
}
