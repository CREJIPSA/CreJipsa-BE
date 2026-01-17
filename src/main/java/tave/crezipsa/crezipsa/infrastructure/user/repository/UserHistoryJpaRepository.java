package tave.crezipsa.crezipsa.infrastructure.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;

public interface UserHistoryJpaRepository extends JpaRepository<UserHistory, Long> {
}
