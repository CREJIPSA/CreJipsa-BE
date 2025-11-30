package tave.crezipsa.crezipsa.domain.user.repository;

import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;

import java.util.List;

public interface UserInterestRepository {
    UserInterest save(UserInterest userInterest);

    UserInterest deleteByUserInterestIdAndUserId(Long interestId, Long userId);

    List<UserInterest> findAllByUserId(Long userId);
}
