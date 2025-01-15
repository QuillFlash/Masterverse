package org.keretrendszer.beadando.masterverse.db_read_helpers;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.CommentsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommentDataRequestHelper
{
    private final CommentsService commentsService;

    public CommentDataRequestHelper(CommentsService commentsService)
    {
        this.commentsService = commentsService;
    }

    public Map<String, Object> processCommentsData(List<Comment> comments,
                                                   @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        Map<Long, Long> likesForComments = new HashMap<>();
        Map<Long, Boolean> hasUserLikedAComment = new HashMap<>();
        for (Comment comment : comments)
        {
            long commentId = comment.getId();
            long likeCount = commentsService.countCommentLikes(commentId);
            likesForComments.put(commentId, likeCount);
            if (currentUser != null)
            {
                long userId = currentUser.getId();
                boolean isCommentLiked = commentsService.hasUserLikedAComment(commentId, userId);
                hasUserLikedAComment.put(commentId, isCommentLiked);
            }
            else
            {
                hasUserLikedAComment.put(commentId, false);
            }
        }
        Map<String, Object> processedData = new HashMap<>();
        processedData.put("likesForComments", likesForComments);
        processedData.put("hasUserLikedAComment", hasUserLikedAComment);
        processedData.put("commentImages", commentsService.getAllCommentImages());
        return processedData;
    }
}
