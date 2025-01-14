package org.keretrendszer.beadando.masterverse.service;
import java.util.List;
import java.util.Optional;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.PostLikes;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.IPostImagesRepository;
import org.keretrendszer.beadando.masterverse.repository.IPostLikesRepository;
import org.keretrendszer.beadando.masterverse.repository.IPostsRepository;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostsService
{
    private final IPostsRepository iPostsRepository;
    private final IPostImagesRepository iPostImagesRepository;
    private final IPostLikesRepository iPostLikesRepository;
    private final IUsersRepository iUsersRepository;

    public PostsService(IPostsRepository iPostsRepository,
                        IPostImagesRepository iPostImagesRepository,
                        IPostLikesRepository iPostLikesRepository,
                        IUsersRepository iUsersRepository)
    {
        this.iPostsRepository = iPostsRepository;
        this.iPostImagesRepository = iPostImagesRepository;
        this.iPostLikesRepository = iPostLikesRepository;
        this.iUsersRepository = iUsersRepository;
    }

    public List<Posts> getAllPosts()
    {
        return iPostsRepository.findAll();
    }

    public List<PostImages> getAllPostImages()
    {
        return iPostImagesRepository.findAll();
    }

    public long countPostLikes(long id)
    {
        return iPostLikesRepository.countByPostId_Id(id);
    }

    public boolean hasUserLikedAPost(long postId, long userId)
    {
        return iPostLikesRepository.existsByPostId_IdAndUserId_Id(postId, userId);
    }

    public Posts getAPostById(long id)
    {
        return iPostsRepository.findById(id).orElse(null);
    }

    public List<Posts> getPostsById(long id)
    {
        return iPostsRepository.findAllByUserId_Id(id);
    }

    public PostImages getAnAttachmentById(long id)
    {
        return iPostImagesRepository.findById(id).orElse(null);
    }

    public List<PostImages> getAttachmentsById(long id)
    {
        return iPostImagesRepository.findAllByPostId_Id(id);
    }

    @Transactional
    public void likePost(long postId, long userId)
    {
        if (!hasUserLikedAPost(postId, userId))
        {
            Posts post = iPostsRepository.findById(postId).orElse(null);
            Users user = iUsersRepository.findById(userId).orElse(null);
            PostLikes newLike = new PostLikes(post, user);
            iPostLikesRepository.save(newLike);
        }
    }

    @Transactional
    public void removeLikeFromPost(long postId, long userId)
    {
        Optional<PostLikes> existingLike = iPostLikesRepository
                .findByPostId_IdAndUserId_Id(postId, userId);
        existingLike.ifPresent(iPostLikesRepository::delete);
    }

    @Transactional
    public void savePost(Posts post)
    {
        iPostsRepository.save(post);
    }
}