package tave.crezipsa.crezipsa.domain.user.repository;

import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;

import java.util.List;
import java.util.Optional;

public interface UserInterestRepository {

    UserInterest save(UserInterest userInterest);
    void deleteByInterestId(Long interestId);
    List<UserInterest> findAllByUserId(Long userId);
    Boolean existsByUserIdAndCategory(Long userId, String category);
    Boolean existByInterestId(Long interestId);
}
