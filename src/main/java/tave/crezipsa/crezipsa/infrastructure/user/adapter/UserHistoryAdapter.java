package tave.crezipsa.crezipsa.infrastructure.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.user.port.UserHistoryPort;
import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;
import tave.crezipsa.crezipsa.domain.user.repository.UserHistoryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserHistoryAdapter implements UserHistoryPort {
    private final UserHistoryRepository userHistoryRepository;

    @Override
    public void saveHistory(long userId, String content) {
        userHistoryRepository.save(new UserHistory(userId, content));
    }

    @Override
    public List<UserHistory> getUserHistory(long userId) {
        return userHistoryRepository.findByUserId(userId);
    }

    @Override
    public void deleteHistoryByHistoryId(long userId, long historyId) {
        userHistoryRepository.deleteByHistoryIdAndUserId(userId,historyId);
    }

    @Override
    public void deleteAllHistoryByUserId(long userId) {
        userHistoryRepository.deleteByUserId(userId);
    }
}
