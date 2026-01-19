package tave.crezipsa.crezipsa.infrastructure.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;

import java.util.List;

public interface UserHistoryJpaRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findTop20ByUserIdOrderByHistoryIdDesc(Long userId);
    void deleteByUserIdAndHistoryId(Long userId, Long historyId);
    void deleteByUserId(Long userId);
}
