package tave.crezipsa.crezipsa.infrastructure.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;
import tave.crezipsa.crezipsa.domain.user.repository.UserInterestRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserInterestRepositoryImpl implements UserInterestRepository {

    private final UserInterestJpaRepository userInterestJpaRepository;

    @Override
    public UserInterest save(UserInterest userInterest) {
        return userInterestJpaRepository.save(userInterest);
    }

    @Override
    public UserInterest deleteByUserInterestIdAndUserId(Long interestId, Long userId){
        return userInterestJpaRepository.deleteByInterestIdAndUserId(interestId,userId);
    }

    @Override
    public List<UserInterest> findAllByUserId(Long userId) {
        return userInterestJpaRepository.findAllByUserId(userId);
    }

}
