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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        return profileMapper.toResponse(getProfileByUserId(userFinder.findById(userId)));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse getProfileByUsername(String username) {
        User user = userFinder.findByUsername(username);
        Profile profile = getProfileByUserId(user);
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse createProfile(UUID userId, CreateProfileRequest request) {
        ensureProfileNotExists(userId);

        Profile entity = profileMapper.toEntity(request);
        User user = userFinder.findById(userId);

        entity.setUser(user);
        profileRepository.save(entity);
        return profileMapper.toResponse(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('USER')")
    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        Profile entity = getProfileByUserId(userFinder.findById(userId));
        profileMapper.toUpdate(entity, request);
        profileRepository.save(entity);
        return profileMapper.toResponse(entity);
    }

    private Profile getProfileByUserId(User user) {
        return profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("profile.not.found", user.getId()));
    }

    private void ensureProfileNotExists(UUID userId) {
        if (profileRepository.existsByUserId(userId)) {
            throw new ConflictException("profile.exists");
        }
    }
}
