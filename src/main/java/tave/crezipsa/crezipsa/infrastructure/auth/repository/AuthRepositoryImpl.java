package tave.crezipsa.crezipsa.infrastructure.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthJpaRepository authJpaRepository;

    @Override
    public Auth save(Auth auth) {
        return authJpaRepository.save(auth);
    }
    @Override
    public Optional<Auth> findByUserId(Long userId){
        return authJpaRepository.findByUserId(userId);
    }
    @Override
    public boolean existsByUserId(Long userId) {
        return authJpaRepository.existsByUserId(userId);
    }

}
