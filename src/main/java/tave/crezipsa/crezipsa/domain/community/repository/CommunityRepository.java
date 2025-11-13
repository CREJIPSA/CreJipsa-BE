package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tave.crezipsa.crezipsa.domain.community.domain.Community;

public interface CommunityRepository {

	Community save(Community community);
	Optional<Community> findById(Long communityId);
	List<Community> findAll();
	void delete(Community community);

}
