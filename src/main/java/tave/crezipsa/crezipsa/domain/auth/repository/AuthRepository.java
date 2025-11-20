package tave.crezipsa.crezipsa.domain.auth.repository;

import tave.crezipsa.crezipsa.domain.auth.entity.Auth;

import java.util.Optional;

public interface AuthRepository {

    Auth save(Auth auth);
    Optional<Auth> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
