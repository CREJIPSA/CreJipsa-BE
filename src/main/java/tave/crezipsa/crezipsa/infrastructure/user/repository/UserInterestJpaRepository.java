package tave.crezipsa.crezipsa.infrastructure.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;

import java.util.List;


public interface UserInterestJpaRepository extends JpaRepository<UserInterest, Long> {
    List<UserInterest> findAllByUserId(Long userId);
    UserInterest save(UserInterest userInterest);
    UserInterest deleteByInterestIdAndUserId(Long interestId, Long userId);
}
