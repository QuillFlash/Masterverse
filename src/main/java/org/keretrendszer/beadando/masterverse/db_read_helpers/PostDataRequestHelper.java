package org.keretrendszer.beadando.masterverse.db_read_helpers;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostDataRequestHelper
{

    private final PostsService postsService;

    public PostDataRequestHelper(PostsService postsService)
    {
        this.postsService = postsService;
    }

    public Map<String, Object> processPostsData(List<Posts> posts,
                                                @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        Map<Long, Long> likesForPosts = new HashMap<>();
        Map<Long, Boolean> hasUserLikedAPost = new HashMap<>();
        for (Posts post : posts)
        {
            long postId = post.getId();
            long likeCount = postsService.countPostLikes(postId);
            likesForPosts.put(postId, likeCount);
            if (currentUser != null)
            {
                long userId = currentUser.getId();
                boolean isPostLiked = postsService.hasUserLikedAPost(postId, userId);
                hasUserLikedAPost.put(postId, isPostLiked);
            }
            else hasUserLikedAPost.put(postId, false);
        }
        Map<String, Object> processedData = new HashMap<>();
        processedData.put("likesForPosts", likesForPosts);
        processedData.put("hasUserLikedAPost", hasUserLikedAPost);
        processedData.put("postImages", postsService.getAllPostImages());
        return processedData;
    }
}