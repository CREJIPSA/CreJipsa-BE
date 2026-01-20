package tave.crezipsa.crezipsa.application.user.port;

import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;

import java.util.List;

public interface UserHistoryPort {
    void saveHistory(long userId, String content);
    List<UserHistory> getUserHistory(long userId);
    void deleteHistoryByHistoryId(long userId, long historyId);
    void deleteAllHistoryByUserId(long userId);
}

