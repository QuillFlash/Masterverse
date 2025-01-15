package org.keretrendszer.beadando.masterverse.db_read_helpers;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.springframework.stereotype.Component;

@Component
public class ValidationHelper
{
    public void validateUserExistence(Users user)
    {
        if (user == null)
        {
            throw new IllegalArgumentException("ERROR: This user does not exist in the database.");
        }
    }
    public void validateCommentExistence(Comment comment, long commentId)
    {
        if (comment == null)
        {
            throw new IllegalArgumentException("ERROR: No comment found with comment ID " + commentId + ".");
        }
    }
    public void validatePostExistence(Posts post, long postId)
    {
        if (post == null)
        {
            throw new IllegalArgumentException("ERROR: No post found with post ID " + postId + ".");
        }
    }
    public void validatePermissions(Users loggedInUser, Users owner, String entityType)
    {
        if (!owner.equals(loggedInUser))
        {
            throw new RuntimeException("You don't have permission to modify this " + entityType + "!");
        }
    }
}