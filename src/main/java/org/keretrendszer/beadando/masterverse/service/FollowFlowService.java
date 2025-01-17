package org.keretrendszer.beadando.masterverse.service;
import org.keretrendszer.beadando.masterverse.model.FollowFlow;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.IFollowFlowRepository;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowFlowService
{
    private final IFollowFlowRepository iFollowFlowRepository;
    private final IUsersRepository iUsersRepository;

    public FollowFlowService(IFollowFlowRepository iFollowFlowRepository, IUsersRepository iUsersRepository)
    {
        this.iFollowFlowRepository = iFollowFlowRepository;
        this.iUsersRepository = iUsersRepository;
    }

    public long countFollowers(long id)
    {
        return iFollowFlowRepository.countByFollowerId(id);
    }

    public List<Users> getFollowingFromDatabase(long userId)
    {
        List<FollowFlow> flows = iFollowFlowRepository.findAllByFollowerId(userId);
        List<Users> following = new ArrayList<>();
        for (FollowFlow flow : flows) following.add(flow.getFollowed());
        return following;
    }

    public List<Users> getFollowersFromDatabase(long userId)
    {
        List<FollowFlow> flows = iFollowFlowRepository.findAllByFollowedId(userId);
        List<Users> followers = new ArrayList<>();
        for (FollowFlow flow : flows) followers.add(flow.getFollower());
        return followers;
    }

    public boolean isUserFollowingOthers(long followerId, long followedId)
    {
        return iFollowFlowRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Transactional
    public void followUser(long userId, long followedId)
    {
        if (!isUserFollowingOthers(userId, followedId))
        {
            Users userFollowingTheOther = iUsersRepository.findById(userId).orElse(null);
            Users followedUser = iUsersRepository.findById(followedId).orElse(null);
            FollowFlow followFlow = new FollowFlow(userFollowingTheOther, followedUser);
            iFollowFlowRepository.save(followFlow);
        }
    }

    @Transactional
    public void removeFollowFromUser(long userId, long followedId)
    {
        Optional<FollowFlow> existingFollowing =
        iFollowFlowRepository.findByFollowerIdAndFollowedId(userId, followedId);
        existingFollowing.ifPresent(iFollowFlowRepository::delete);
    }
}
