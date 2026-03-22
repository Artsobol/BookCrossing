package io.github.artsobol.bookcrossing.feature.author.mapper;

import io.github.artsobol.bookcrossing.config.persistence.MapStructConfig;
import io.github.artsobol.bookcrossing.feature.author.dto.AuthorResponse;
import io.github.artsobol.bookcrossing.feature.author.dto.CreateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.dto.UpdateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.entity.Author;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapStructConfig.class)
public interface AuthorMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "name")
    @Mapping(target = "slug")
    AuthorResponse toResponse(Author author);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name")
    @Mapping(target = "slug")
    Author toEntity(CreateAuthorRequest request);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name")
    @Mapping(target = "slug")
    void update(@MappingTarget Author author, UpdateAuthorRequest request);
}
