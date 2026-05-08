package com.example.profile_bankend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users2")
@CrossOrigin(originPatterns = "*")
public class UserProfileController2 {
    private final UserProfileService userProfileService;

    public UserProfileController2(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
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
