package io.github.artsobol.bookcrossing.feature.profile.mapper;

import io.github.artsobol.bookcrossing.config.persistence.MapStructConfig;
import io.github.artsobol.bookcrossing.feature.profile.dto.request.CreateProfileRequest;
import io.github.artsobol.bookcrossing.feature.profile.dto.request.UpdateProfileRequest;
import io.github.artsobol.bookcrossing.feature.profile.dto.response.ProfileResponse;
import io.github.artsobol.bookcrossing.feature.profile.entity.Profile;
import io.github.artsobol.bookcrossing.feature.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapStructConfig.class, uses = UserMapper.class)
public interface ProfileMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "bio")
    Profile toEntity(CreateProfileRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "bio")
    @Mapping(target = "user")
    ProfileResponse toResponse(Profile profile);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "bio")
    void toUpdate(@MappingTarget Profile profile, UpdateProfileRequest request);
}
