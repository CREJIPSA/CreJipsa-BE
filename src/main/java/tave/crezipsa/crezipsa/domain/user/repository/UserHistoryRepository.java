package tave.crezipsa.crezipsa.domain.user.repository;

import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;

import java.util.List;

public interface UserHistoryRepository {
    void save(UserHistory userHistory);
    List<UserHistory> findByUserId(long userId);
    void deleteByHistoryIdAndUserId(long userId, long historyId);
    void deleteByUserId(long userId);
}
