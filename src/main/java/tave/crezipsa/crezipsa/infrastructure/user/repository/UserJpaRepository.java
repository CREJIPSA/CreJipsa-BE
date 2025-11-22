package tave.crezipsa.crezipsa.infrastructure.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.infrastructure.user.entity.UserJpaEntity;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {


    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findById(Long userId);
    Optional<UserJpaEntity> findByNickName(String nickName);
}
