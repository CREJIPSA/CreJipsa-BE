package tave.crezipsa.crezipsa.infrastructure.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.user.entity.UserHistory;
import tave.crezipsa.crezipsa.domain.user.repository.UserHistoryRepository;

@Repository
@RequiredArgsConstructor
public class UserHistoryRespositoryImpl implements UserHistoryRepository {

    private final UserHistoryJpaRepository userHistoryJpaRepository;

    @Override
    public void save(UserHistory userHistory) {
        userHistoryJpaRepository.save(userHistory);
    }
}
