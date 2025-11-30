package tave.crezipsa.crezipsa.presentation.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserUpdateRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.usecase.UserUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
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

    @PatchMapping("/update")
    public GlobalResponseDto update(
            @AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateRequest request){

        userUsecase.update(user.getUserId(),request);
        return GlobalResponseDto.success();
    }

}
