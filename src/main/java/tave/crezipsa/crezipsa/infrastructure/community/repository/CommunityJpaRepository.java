package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommunityJpaRepository extends JpaRepository<Community, Long> {

	List<Community> findByWriterId(Long writerId);
	List<Community> findByField(CommunityField field);
}
