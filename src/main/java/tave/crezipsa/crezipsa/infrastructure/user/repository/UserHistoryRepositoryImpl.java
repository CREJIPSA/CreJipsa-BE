package tave.crezipsa.crezipsa.infrastructure.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;
import tave.crezipsa.crezipsa.domain.user.repository.UserHistoryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserHistoryRepositoryImpl implements UserHistoryRepository {

    private final UserHistoryJpaRepository userHistoryJpaRepository;

    @Override
    public void save(UserHistory userHistory) {
        userHistoryJpaRepository.save(userHistory);
    }

    @Override
    public List<UserHistory> findByUserId(long userId) {
        return userHistoryJpaRepository.findTop20ByUserIdOrderByHistoryIdDesc(userId);
    }

    @Override
    public void deleteByHistoryIdAndUserId(long userId, long historyId) {
        userHistoryJpaRepository.deleteByUserIdAndHistoryId(userId, historyId);
    }

    @Override
    public void deleteByUserId(long userId) {
        userHistoryJpaRepository.deleteByUserId(userId);
    }
}
