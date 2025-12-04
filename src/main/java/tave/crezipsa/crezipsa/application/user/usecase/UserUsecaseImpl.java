package tave.crezipsa.crezipsa.application.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.user.dto.request.UserInterestRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserUpdateRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserInterestResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserUpdateResponse;
import tave.crezipsa.crezipsa.domain.user.command.UserSignUpCommand;
import tave.crezipsa.crezipsa.domain.user.command.UserUpdateCommand;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;
import tave.crezipsa.crezipsa.domain.user.repository.UserInterestRepository;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUsecaseImpl implements UserUsecase {

    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;

    @Override
    public UserSignUpResponse signUp(UserSignUpRequest request) {

        if(userRepository.existsByEmail(request.email())){
           throw  new CommonException(ErrorCode.USER_ALREADY_EXISTS_EMAIL);
        }
        if(userRepository.existsByNickName(request.nickName())){
            throw new CommonException(ErrorCode.USER_ALREADY_EXISTS_NICKNAME);
        }

        User user = User.createFromUser(UserSignUpCommand.from(request));

        return UserSignUpResponse.from(userRepository.save(user));
    }

    @Override
    public UserUpdateResponse update(Long userId, UserUpdateRequest request) {

        User user =  userRepository.findById(userId).
                orElseThrow(() -> new CommonException(ErrorCode.USER_INVALID_ID));

        user.updateFromUser(UserUpdateCommand.from(request));

        return UserUpdateResponse.from(userRepository.save(user));

    }

    @Override
    public UserInterestResponse addUserInterest(Long userId, UserInterestRequest request) {

        if (userInterestRepository.existsByUserIdAndCategory(userId, request.category())) {
            throw new CommonException(ErrorCode.ALREADY_INTEREST);
        }

        UserInterest newUserInterest = userInterestRepository.save( UserInterest.create(userId, request.category()) );

        return UserInterestResponse.from(newUserInterest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserInterestResponse> getUserInterest(Long userId) {

        List<UserInterest> interests = userInterestRepository.findAllByUserId(userId);
        List<UserInterestResponse> responses = new ArrayList<>();

        for (UserInterest interest : interests) {
            responses.add(UserInterestResponse.from(interest));
        }

        return responses;
    }

    @Override
    public void deleteUserInterest(Long userId, UserInterestRequest request) {
        UserInterest userInterest = userInterestRepository.findByUserIdAndCategoryId(userId,request.category())
                .orElseThrow(() -> new CommonException(ErrorCode.INVALD_INTEREST));

        userInterestRepository.deleteByInterestId(userInterest.getInterestId());
    }

}
