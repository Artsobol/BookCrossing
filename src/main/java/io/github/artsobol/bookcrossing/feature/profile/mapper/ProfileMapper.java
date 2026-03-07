package io.github.artsobol.bookcrossing.feature.profile.mapper;

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

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ProfileMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "bio", source = "bio")
    Profile toEntity(CreateProfileRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "bio", source = "bio")
    @Mapping(target = "user", source = "user")
    ProfileResponse toResponse(Profile profile);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastName", source = "lastName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "bio", source = "bio", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdate(@MappingTarget Profile profile, UpdateProfileRequest request);
}
