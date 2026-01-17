package tave.crezipsa.crezipsa.application.user.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.User;

public record UserResponse(
    String nickName,
    String email

){
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getNickName(),
                user.getEmail()
        );
    }
}
