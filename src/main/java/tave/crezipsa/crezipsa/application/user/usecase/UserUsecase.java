package tave.crezipsa.crezipsa.application.user.usecase;

import tave.crezipsa.crezipsa.application.user.dto.request.UserInterestRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserUpdateRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserInterestResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserUpdateResponse;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;


public interface UserUsecase {
    UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest);
    UserUpdateResponse update(Long userId, UserUpdateRequest request);
    UserInterestResponse addUserInsert(Long userId, UserInterestRequest request);
}
