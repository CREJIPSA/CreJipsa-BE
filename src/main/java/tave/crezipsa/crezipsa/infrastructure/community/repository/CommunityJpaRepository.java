package tave.crezipsa.crezipsa.infrastructure.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.domain.community.domain.Community;

public interface CommunityJpaRepository extends JpaRepository<Community, Long> {
}
