package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.FollowFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IFollowFlowRepository extends JpaRepository<FollowFlow, Long>
{
    long countByFollowerId(long followedId);
    List<FollowFlow> findAllByFollowerId(long followerId);
    List<FollowFlow> findAllByFollowedId(long followedId);
    boolean existsByFollowerIdAndFollowedId(long followerId, long followedId);
    Optional<FollowFlow> findByFollowerIdAndFollowedId(long userId, long followerId);
}
