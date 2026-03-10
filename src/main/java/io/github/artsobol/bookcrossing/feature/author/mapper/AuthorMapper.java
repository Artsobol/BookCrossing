package io.github.artsobol.bookcrossing.feature.author.mapper;

import io.github.artsobol.bookcrossing.feature.author.dto.AuthorResponse;
import io.github.artsobol.bookcrossing.feature.author.dto.CreateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.dto.UpdateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.entity.Author;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    AuthorResponse toResponse(Author author);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    Author toEntity(CreateAuthorRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "slug", source = "slug",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Author author, UpdateAuthorRequest request);
}
