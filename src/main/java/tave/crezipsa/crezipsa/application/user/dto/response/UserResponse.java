package tave.crezipsa.crezipsa.application.user.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.User;

public record UserSignUpResponse (
    String nickName,
    String email

){
    public static UserSignUpResponse from(User user) {
        return new UserSignUpResponse(
                user.getNickName(),
                user.getEmail()
        );
    }
}
