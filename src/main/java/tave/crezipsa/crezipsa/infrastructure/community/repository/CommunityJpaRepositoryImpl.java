package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;


@Repository
@RequiredArgsConstructor
public class CommunityJpaRepositoryImpl implements CommunityRepository {

	private final CommunityJpaRepository communityJpaRepository;

	@Override
	public Community save(Community community) {
		return communityJpaRepository.save(community);
	}

	@Override
	public Optional<Community> findById(Long communityId) {
		return communityJpaRepository.findById(communityId);
	}

	@Override
	public List<Community> findAll() {
		return communityJpaRepository.findAll();
	}

	@Override
	public void delete(Community community) {
		communityJpaRepository.delete(community);
	}

}
