package tave.crezipsa.crezipsa.infrastructure.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;

import java.util.List;
import java.util.Optional;


public interface UserInterestJpaRepository extends JpaRepository<UserInterest, Long> {
    List<UserInterest> findAllByUserId(Long userId);
    UserInterest save(UserInterest userInterest);
    Boolean deleteByInterestId(Long interestId);
    boolean existsByUserIdAndCategory(Long userId, String category);
    Optional<UserInterest> findByUserIdAndCategory(Long userId, String category);
}
