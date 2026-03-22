package io.github.artsobol.bookcrossing.feature.role.mapper;

import io.github.artsobol.bookcrossing.config.persistence.MapStructConfig;
import io.github.artsobol.bookcrossing.feature.role.dto.response.RoleResponse;
import io.github.artsobol.bookcrossing.feature.role.entity.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface RoleMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name")
    RoleResponse toResponse(Role role);
}
