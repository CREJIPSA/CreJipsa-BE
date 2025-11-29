package tave.crezipsa.crezipsa.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateCommand {

    private final String activeInsta;
    private final String activeYoutube;
    private final String activeTiktok;
    private final Platform mainPlatform;

}
