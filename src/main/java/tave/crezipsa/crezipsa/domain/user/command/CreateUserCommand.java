package tave.crezipsa.crezipsa.domain.user.command;

import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

public record CreateUserCommand(
    String nickName,
    String email,
    Gender gender,
    boolean role,
    LocalDate birth,
    String activeYoutube,
    String activeInsta,
    String activeTiktok,
    Platform mainPlatform
) {}
