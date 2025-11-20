package tave.crezipsa.crezipsa.domain.user.repository;

import tave.crezipsa.crezipsa.domain.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    //리턴 값이 null일 수도
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    List<User> findAllById(Collection<Long> userIds);
}
