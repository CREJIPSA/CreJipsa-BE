package tave.crezipsa.crezipsa.presentation.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.request.UserUpdateRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserInterestResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserResponse;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.application.user.usecase.UserUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
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
            @Valid @RequestBody UserSignUpRequest request) {

        UserSignUpResponse response = userUsecase.signUp(request);
        return GlobalResponseDto.success(response);
    }

    @PatchMapping("/update")
    public GlobalResponseDto update(
            @AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateRequest request) {

        return GlobalResponseDto.success(userUsecase.update(user.getUserId(), request));
    }

    @DeleteMapping("/update")
    public GlobalResponseDto<Void> deletePlatform(
            @AuthenticationPrincipal User user,
            @RequestParam Platform platform
    ) {
        userUsecase.deletePlatform(user.getUserId(), platform);
        return GlobalResponseDto.success(null);
    }

    @PostMapping("/interest/{category}")
    public GlobalResponseDto insertInterest(
            @AuthenticationPrincipal User user, @PathVariable String category) {

        UserInterestResponse interestResponse = userUsecase.addUserInterest(user.getUserId(), category);
        return GlobalResponseDto.success(interestResponse);
    }

    @DeleteMapping("/interest/{interestId}")
    public GlobalResponseDto deleteInterest(
            @AuthenticationPrincipal User user, @PathVariable Long interestId) {

        userUsecase.deleteUserInterest(user.getUserId(), interestId);
        return GlobalResponseDto.success();
    }

    @GetMapping("/interest")
    public GlobalResponseDto getInterest(@AuthenticationPrincipal User user) {

        List<UserInterestResponse> userInterest = userUsecase.getUserInterest(user.getUserId());
        return GlobalResponseDto.success(userInterest);
    }

    @DeleteMapping("/me")
    public GlobalResponseDto deleteUser(@AuthenticationPrincipal User user) {

        userUsecase.deleteUser(user.getUserId());
        return GlobalResponseDto.success();
    }
    @GetMapping("/me")
    public GlobalResponseDto<UserResponse> getUserInfo(@AuthenticationPrincipal User user){
        return GlobalResponseDto.success(userUsecase.getUser(user));
    }
}

