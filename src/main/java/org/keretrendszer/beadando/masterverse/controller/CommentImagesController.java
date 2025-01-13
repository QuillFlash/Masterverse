package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.model.CommentImages;
import org.keretrendszer.beadando.masterverse.service.CommentsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/images"})
public class CommentImagesController
{
    private final CommentsService commentsService;

    public CommentImagesController(CommentsService commentsService)
    {
        this.commentsService = commentsService;
    }

    @GetMapping("/comment_image_{id}.jpg")
    public ResponseEntity<byte[]> getImageById(@PathVariable long id)
    {
        CommentImages commentImages = commentsService.getAttachmentsByUserId(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
        .body(commentImages.getImage());
    }
}
