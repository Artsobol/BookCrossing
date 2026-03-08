package io.github.artsobol.bookcrossing.feature.profile.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.profile.dto.request.CreateProfileRequest;
import io.github.artsobol.bookcrossing.feature.profile.dto.request.UpdateProfileRequest;
import io.github.artsobol.bookcrossing.feature.profile.dto.response.ProfileResponse;
import io.github.artsobol.bookcrossing.feature.profile.entity.Profile;
import io.github.artsobol.bookcrossing.feature.profile.mapper.ProfileMapper;
import io.github.artsobol.bookcrossing.feature.profile.repository.ProfileRepository;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.feature.user.service.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserFinder userFinder;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse getProfileByUserId(UUID userId) {
        log.debug("Getting profile by user id: {}", userId);
        return profileMapper.toResponse(getProfileByUserId(userFinder.findById(userId)));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse getProfileByUsername(String username) {
        log.debug("Getting profile by username: {}", username);
        User user = userFinder.findByUsername(username);
        Profile profile = getProfileByUserId(user);
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse createProfile(UUID userId, CreateProfileRequest request) {
        log.info("Creating profile for user");
        ensureProfileNotExists(userId);

        Profile entity = profileMapper.toEntity(request);
        User user = userFinder.findById(userId);

        entity.setUser(user);
        profileRepository.save(entity);
        log.info("Profile created for user: {}", user.getUsername());

        return profileMapper.toResponse(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        log.info("Updating profile for user");
        Profile entity = getProfileByUserId(userFinder.findById(userId));
        profileMapper.toUpdate(entity, request);
        profileRepository.save(entity);
        log.info("Profile updated for user");

        return profileMapper.toResponse(entity);
    }

    private Profile getProfileByUserId(User user) {
        log.debug("Getting profile by user id: {}", user.getId());
        return profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("profile.not.found", user.getId()));
    }

    private void ensureProfileNotExists(UUID userId) {
        log.debug("Checking if profile exists for user");
        if (profileRepository.existsByUserId(userId)) {
            throw new ConflictException("profile.exists");
        }
    }
}
