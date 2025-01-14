package org.keretrendszer.beadando.masterverse.service;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.CommentImages;
import org.keretrendszer.beadando.masterverse.model.CommentLikes;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.ICommentImagesRepository;
import org.keretrendszer.beadando.masterverse.repository.ICommentLikesRepository;
import org.keretrendszer.beadando.masterverse.repository.ICommentRepository;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsService
{
    private final ICommentRepository iCommentRepository;
    private final ICommentImagesRepository iCommentImagesRepository;
    private final ICommentLikesRepository iCommentLikesRepository;
    private final IUsersRepository iUsersRepository;

    public CommentsService(ICommentRepository iCommentRepository,
                           ICommentImagesRepository iCommentImagesRepository,
                           ICommentLikesRepository iCommentLikesRepository,
                           IUsersRepository iUsersRepository)
    {
        this.iCommentRepository = iCommentRepository;
        this.iCommentImagesRepository = iCommentImagesRepository;
        this.iCommentLikesRepository = iCommentLikesRepository;
        this.iUsersRepository = iUsersRepository;
    }
    
    public List<Comment> getAllComments()
    {
        return iCommentRepository.findAll();
    }

    public List<CommentImages> getAllCommentImages()
    {
        return iCommentImagesRepository.findAll();
    }

    public long countCommentLikes(long id)
    {
        return iCommentLikesRepository.countByCommentId_Id(id);
    }

    public Comment getACommentById(long id)
    {
        return iCommentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsById(long id)
    {
        return iCommentRepository.findAllByPostId_Id(id);
    }

    public CommentImages getAnAttachmentById(long id)
    {
        return iCommentImagesRepository.findById(id).orElse(null);
    }

    public List<CommentImages> getAttachmentsById(long id)
    {
        return iCommentImagesRepository.findAllByCommentId_Id(id);
    }

    public boolean hasUserLikedAComment(long postId, long userId)
    {
        return iCommentLikesRepository.existsByCommentId_IdAndUserId_Id(postId, userId);
    }

    @Transactional
    public void likeComment(long commentId, long userId)
    {
        if (!hasUserLikedAComment(commentId, userId))
        {
            Comment comment = iCommentRepository.findById(commentId).orElse(null);
            Users user = iUsersRepository.findById(userId).orElse(null);
            CommentLikes newLike = new CommentLikes(comment, user);
            iCommentLikesRepository.save(newLike);
        }
    }

    @Transactional
    public void removeLikeFromComment(long commentId, long userId)
    {
        Optional<CommentLikes> existingLike = iCommentLikesRepository
                .findByCommentId_IdAndUserId_Id(commentId, userId);
        existingLike.ifPresent(iCommentLikesRepository::delete);
    }

    @Transactional
    public void saveComment(Comment comment)
    {
        iCommentRepository.save(comment);
    }
}
