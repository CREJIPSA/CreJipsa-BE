package tave.crezipsa.crezipsa.presentation.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.usecase.UserUsecase;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserUsecase userUsecase;

    @PostMapping("/signUp")
    public GlobalResponseDto<UserSignUpResponse> signUp(
            @Valid @RequestBody UserSignUpRequest request){
        UserSignUpResponse response  = userUsecase.signUp(request);

        return GlobalResponseDto.success(response);
    }

}
