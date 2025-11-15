package tave.crezipsa.crezipsa.domain.community.repository;

import java.util.List;
import java.util.Optional;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommunityRepository {

	Community save(Community community);
	Optional<Community> findById(Long communityId);
	List<Community> findAll();
	void delete(Community community);

	List<Community> findByWriterId(Long writerId);
	List<Community> findByField(CommunityField field);
}
