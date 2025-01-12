package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.repository.IPostImagesRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/images"})
public class PostImagesController
{
    private final IPostImagesRepository iPostImagesRepository;

    public PostImagesController(IPostImagesRepository iPostImagesRepository)
    {
        this.iPostImagesRepository = iPostImagesRepository;
    }

    @GetMapping({"/post_image_{id}.jpg"})
    public ResponseEntity<byte[]> getImageById(@PathVariable long id)
    {
        PostImages postImages = iPostImagesRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Image not found"));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
        .body(postImages.getImage());
    }
}