package io.github.artsobol.bookcrossing.feature.profile.service;

import io.github.artsobol.bookcrossing.feature.profile.dto.request.CreateProfileRequest;
import io.github.artsobol.bookcrossing.feature.profile.dto.request.UpdateProfileRequest;
import io.github.artsobol.bookcrossing.feature.profile.dto.response.ProfileResponse;

import java.util.UUID;

public interface ProfileService {

    ProfileResponse getProfileByUserId(UUID userId);

    ProfileResponse getProfileByUsername(String username);

    ProfileResponse createProfile(UUID userId, CreateProfileRequest request);

    ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}
