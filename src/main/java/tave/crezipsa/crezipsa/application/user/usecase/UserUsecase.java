package tave.crezipsa.crezipsa.application.user.usecase;

import tave.crezipsa.crezipsa.application.user.dto.request.UserInterestRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserUpdateRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserInterestResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserUpdateResponse;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

import java.util.List;


public interface UserUsecase {
    UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest);
    UserUpdateResponse update(Long userId, UserUpdateRequest request);
    UserInterestResponse addUserInterest(Long userId, String category);
    List<UserInterestResponse> getUserInterest(Long userId);
    void deleteUserInterest(Long UserId, Long interestId);
}
