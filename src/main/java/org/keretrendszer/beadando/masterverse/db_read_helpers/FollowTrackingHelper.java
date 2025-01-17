package org.keretrendszer.beadando.masterverse.db_read_helpers;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.FollowFlowService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FollowTrackingHelper
{
    private final FollowFlowService followFlowService;

    public FollowTrackingHelper(FollowFlowService followFlowService)
    {
        this.followFlowService = followFlowService;
    }

    public Map<String, Object> processFollowData(long id, @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long currentUserId = currentUser.getId();
        List<Users> followers = followFlowService.getFollowersFromDatabase(currentUserId);
        List<Users> following = followFlowService.getFollowingFromDatabase(currentUserId);
        long followerCount = followFlowService.countFollowers(currentUserId);
        boolean isUserFollowingOthers = followFlowService.isUserFollowingOthers(currentUserId, id);
        Map<String, Object> processedData = new HashMap<>();
        processedData.put("followers", followers);
        processedData.put("following", following);
        processedData.put("followerCount", followerCount);
        processedData.put("isUserFollowingOthers", isUserFollowingOthers);
        return processedData;
    }
}
