package io.github.artsobol.bookcrossing.feature.user.mapper;

import io.github.artsobol.bookcrossing.feature.role.mapper.RoleMapper;
import io.github.artsobol.bookcrossing.feature.user.dto.response.UserResponse;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "roles", source = "roles")
    UserResponse toResponse(User user);
}
