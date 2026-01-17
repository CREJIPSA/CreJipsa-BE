package tave.crezipsa.crezipsa.domain.user.repository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;

public interface UserHistoryRepository {
    void save(UserHistory userHistory);
}
