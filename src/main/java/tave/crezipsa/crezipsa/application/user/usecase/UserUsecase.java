package tave.crezipsa.crezipsa.application.user.usecase;

import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;


public interface UserUsecase {
    UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest);
}
