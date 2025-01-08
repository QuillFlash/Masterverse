package org.keretrendszer.beadando.masterverse.service;
import java.util.List;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.repository.IPostImagesRepository;
import org.keretrendszer.beadando.masterverse.repository.IPostsRepository;
import org.springframework.stereotype.Service;

@Service
public class PostsService
{
    private final IPostsRepository iPostsRepository;
    private final IPostImagesRepository iPostImagesRepository;

    public PostsService(IPostsRepository iPostsRepository,
                        IPostImagesRepository iPostImagesRepository)
    {
        this.iPostsRepository = iPostsRepository;
        this.iPostImagesRepository = iPostImagesRepository;
    }

    public List<Posts> getAllPosts()
    {
        return iPostsRepository.findAll();
    }

    public List<PostImages> getAllPostImages()
    {
        return iPostImagesRepository.findAll();
    }

    public Posts getPostsByUserId(long id)
    {
        return iPostsRepository.findById(id).orElse(null);
    }

    public PostImages getAttachmentsByUserId(long id)
    {
        return iPostImagesRepository.findById(id).orElse(null);
    }
}