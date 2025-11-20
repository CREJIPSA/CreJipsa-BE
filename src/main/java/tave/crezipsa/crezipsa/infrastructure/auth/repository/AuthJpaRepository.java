package tave.crezipsa.crezipsa.infrastructure.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;

import java.util.Optional;

@Repository
public interface AuthJpaRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
}
