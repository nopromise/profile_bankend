package com.example.profile_bankend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/hello111")
    public String  hello() {
        return "hello k88888112288s";
    }


    @GetMapping("/ha")
    public String  haha() {
        return "haha---- k88888112288s";
    }


    @GetMapping("/comeon")
    public String  comeon() {
        return "comeon---- k88888112288s";
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfile create(@RequestBody UserProfileRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        return userProfileService.create(request);
    }



    @GetMapping("/{id}")
    public UserProfile getById(@PathVariable Long id) {
        return userProfileService.getById(id);
    }

    @PutMapping("/{id}")
    public UserProfile update(@PathVariable Long id, @RequestBody UserProfileRequest request) {
        return userProfileService.update(id, request);
    }
}
