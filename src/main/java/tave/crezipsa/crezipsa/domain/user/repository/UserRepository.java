package tave.crezipsa.crezipsa.domain.user.repository;

import tave.crezipsa.crezipsa.domain.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    List<User> findAllById(Collection<Long> userIds);
    Boolean existsByEmail(String email);
    Boolean existsByNickName(String NickName);
    void deleteById(long userId);

}
