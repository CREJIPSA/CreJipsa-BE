package tave.crezipsa.crezipsa.application.user.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.UserInterest;

public record UserInterestResponse(
        Long userId,
        Long interestId,
        String category
) {
    public static UserInterestResponse from(UserInterest userInterest) {
        return new UserInterestResponse(
                userInterest.getUserId(),
                userInterest.getInterestId(),
                userInterest.getCategory()
        );
    }
}
