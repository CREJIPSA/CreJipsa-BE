package tave.crezipsa.crezipsa.application.user.port;

import java.util.List;

public interface UserInterestPort {
    List<String> getInterests(long userId);
}
