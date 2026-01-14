package tave.crezipsa.crezipsa.infrastructure.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;
import tave.crezipsa.crezipsa.domain.user.repository.UserInterestRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserInterestRepositoryImpl implements UserInterestRepository {

    private final UserInterestJpaRepository userInterestJpaRepository;

    @Override
    public UserInterest save(UserInterest userInterest) {
        return userInterestJpaRepository.save(userInterest);
    }

    @Override
    public List<UserInterest> saveAll(List<UserInterest> userInterests) {
        return userInterestJpaRepository.saveAll(userInterests);
    }

    @Override
    public void deleteByInterestId(Long interestId) {
        userInterestJpaRepository.deleteByInterestId(interestId);
    }

    @Override
    public List<UserInterest> findAllByUserId(Long userId) {
        return userInterestJpaRepository.findAllByUserId(userId);
    }

    @Override
    public Boolean existsByUserIdAndCategory(Long userId, String category) {
        return userInterestJpaRepository.existsByUserIdAndCategory(userId, category);
    }
    
    @Override
    public Boolean existByInterestId(Long interestId){
        return userInterestJpaRepository.existsById(interestId);
    }
}
