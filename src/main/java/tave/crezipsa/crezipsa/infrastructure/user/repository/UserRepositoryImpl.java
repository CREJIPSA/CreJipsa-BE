package tave.crezipsa.crezipsa.infrastructure.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.infrastructure.user.entity.UserJpaEntity;
import tave.crezipsa.crezipsa.infrastructure.user.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toJpaUserEntity(user);
        UserJpaEntity saved = userJpaRepository.save(entity);

        return UserMapper.toUserDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
         .map(UserMapper::toUserDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId)
            .map(UserMapper::toUserDomain);
    }

    @Override
    public List<User> findAllById(Collection<Long> userIds) {
        return userJpaRepository.findAllById(userIds).stream()
            .map(UserMapper::toUserDomain)
            .toList();
    }

    @Override
    public Optional<User> findByNickName(String nickName) {
        return userJpaRepository.findByNickName(nickName)
                .map(UserMapper::toUserDomain);
    }
}
