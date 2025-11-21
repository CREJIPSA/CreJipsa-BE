package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

	private final LikeJpaRepository likeJpaRepository;

	@Override
	public Like save(Like like) {
		return likeJpaRepository.save(like);
	}

	@Override
	public Optional<Like> findById(LikeId likeId) {
		return likeJpaRepository.findById(likeId);
	}

	@Override
	public void delete(Like like) {
		likeJpaRepository.delete(like);
	}

	@Override
	public boolean existsById(LikeId likeId) {
		return likeJpaRepository.existsById(likeId);
	}

	@Override
	public boolean existsByUserIdAndCommunityId(Long userId, Long communityId) {
		return likeJpaRepository.existsByUserIdAndCommunityId(userId, communityId);
	}

	@Override
	public void deleteByUserIdAndCommunityId(Long userId, Long communityId) {
		likeJpaRepository.deleteByUserIdAndCommunityId(userId, communityId);
	}

	@Override
	public long countByCommunityId(Long communityId) {
		return likeJpaRepository.countByCommunityId(communityId);
	}

	@Override
	public Page<Like> findAllByUserId(Long userId, Pageable pageable) {
		return likeJpaRepository.findAllByUserId(userId, pageable);
	}
}
