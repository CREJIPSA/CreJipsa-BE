package tave.crezipsa.crezipsa.presentation.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.application.user.dto.request.UserInterestRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserUpdateRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserInterestResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.usecase.UserUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

import java.util.List;

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

    @PostMapping("/interest")
    public GlobalResponseDto insertInterest(
            @AuthenticationPrincipal User user, @Valid @RequestBody UserInterestRequest request){

        UserInterestResponse interestResponse = userUsecase.addUserInterest(user.getUserId(),request);
        return GlobalResponseDto.success(interestResponse);
    }

    @DeleteMapping("/interest")
    public GlobalResponseDto deleteInterest(
            @AuthenticationPrincipal User user, @Valid @RequestBody UserInterestRequest request){

        userUsecase.deleteUserInterest(user.getUserId(),request);
        return GlobalResponseDto.success();
    }

    @GetMapping("/interest")
    public GlobalResponseDto getInterest(
            @AuthenticationPrincipal User user, @Valid UserInterestRequest request){

        List<UserInterestResponse> userInterest =  userUsecase.getUserInterest(user.getUserId());
        return GlobalResponseDto.success(userInterest);
    }
}
