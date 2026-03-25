package io.github.artsobol.bookcrossing.feature.profile.mapper;

import io.github.artsobol.bookcrossing.config.persistence.MapStructConfig;
import io.github.artsobol.bookcrossing.feature.profile.dto.response.ProfileResponse;
import io.github.artsobol.bookcrossing.feature.profile.entity.Profile;
import io.github.artsobol.bookcrossing.feature.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = UserMapper.class)
public interface ProfileMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "bio")
    @Mapping(target = "user")
    ProfileResponse toResponse(Profile profile);
}
