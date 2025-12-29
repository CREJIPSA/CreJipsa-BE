package tave.crezipsa.crezipsa.infrastructure.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public interface CommunityJpaRepository extends JpaRepository<Community, Long> {

	List<Community> findByWriterId(Long writerId);
	List<Community> findByField(CommunityField field);

	@Query("""
	select c
	from Community c
	where c.writerId = :writerId
	order by c.createdAt desc
""")
	Page<Community> findMyCommunitiesLatest(
		@Param("writerId") Long writerId,
		Pageable pageable
	);

	@Query("""
	select c
	from Community c
	where c.writerId = :writerId
	order by c.likeCount desc, c.createdAt desc
""")
	Page<Community> findMyCommunitiesPopular(
		@Param("writerId") Long writerId,
		Pageable pageable
	);


}
