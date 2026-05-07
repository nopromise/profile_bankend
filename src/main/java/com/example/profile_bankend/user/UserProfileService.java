package com.example.profile_bankend.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserProfileService {
    private final UserProfileMapper userProfileMapper;

    public UserProfileService(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Transactional
    public UserProfile create(UserProfileRequest request) {
        UserProfile userProfile = toUserProfile(request);
        userProfileMapper.insert(userProfile);
        return userProfileMapper.findById(userProfile.getId());
    }

    @Transactional
    public UserProfile update(Long id, UserProfileRequest request) {
        UserProfile userProfile = toUserProfile(request);
        userProfile.setId(id);
        int updated = userProfileMapper.updateById(userProfile);
        if (updated == 0) {
            throw new IllegalArgumentException("User not found by id: " + id);
        }
        return userProfileMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public UserProfile getById(Long id) {
        UserProfile userProfile = userProfileMapper.findById(id);
        if (userProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by id: " + id);
        }
        return userProfile;
    }

    private UserProfile toUserProfile(UserProfileRequest request) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(request.getId());
        userProfile.setUsername(request.getUsername());
        userProfile.setGender(request.getGender());
        userProfile.setEmail(request.getEmail());
        return userProfile;
    }
}
