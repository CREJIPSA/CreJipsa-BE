package tave.crezipsa.crezipsa.infrastructure.user.adapter;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.user.port.UserInterestPort;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.infrastructure.user.repository.UserInterestJpaRepository;
import tave.crezipsa.crezipsa.infrastructure.user.repository.UserJpaRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserInterestAdapter implements UserInterestPort {
    private final UserInterestJpaRepository userInterestJpaRepository;

    @Override
    public List<String> getInterests(long userId) {
        List<UserInterest> interests = userInterestJpaRepository.findAllByUserId(userId);

        if (interests.isEmpty()) {
            throw new CommonException(ErrorCode.USER_INTEREST_NOT_SET);
        }

        return interests.stream()
                .map(UserInterest::getCategory)
                .toList();
    }
}