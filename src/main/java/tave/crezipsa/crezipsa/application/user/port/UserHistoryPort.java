package tave.crezipsa.crezipsa.application.user.port;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserHistoryRepository;

public interface UserHistoryPort {

    void saveHistory(long userId, String content);

}
