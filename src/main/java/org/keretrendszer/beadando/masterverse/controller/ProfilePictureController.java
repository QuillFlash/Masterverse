package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/images"})
public class ProfilePictureController
{
    private final IUsersRepository iUsersRepository;

    public ProfilePictureController(IUsersRepository iUsersRepository)
    {
        this.iUsersRepository = iUsersRepository;
    }

    @GetMapping("/profile_picture_{id}.jpg")
    public ResponseEntity<byte[]> getProfilePictureById(@PathVariable long id)
    {
        Users profilePicture = iUsersRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Image not found"));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                             .body(profilePicture.getProfilePicture());
    }
}
