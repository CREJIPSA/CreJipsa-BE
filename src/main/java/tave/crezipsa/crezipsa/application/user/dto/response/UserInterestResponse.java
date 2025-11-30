package tave.crezipsa.crezipsa.application.user.dto.response;

public record UserInterestResponse(
        Long userId,
        Long interestId,
        String category
) {
}
