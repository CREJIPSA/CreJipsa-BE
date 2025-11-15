package tave.crezipsa.crezipsa.domain.auth.repository;

import tave.crezipsa.crezipsa.domain.auth.entity.Auth;

import java.util.Optional;

public interface AuthRepository {

    Optional<Auth> findByUserId(Long userId);

    Auth save(Auth auth);

    boolean existsByUserId(Long userId);
}
