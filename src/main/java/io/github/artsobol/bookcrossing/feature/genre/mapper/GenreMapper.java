package io.github.artsobol.bookcrossing.feature.genre.mapper;

import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "description", source = "description")
    GenreResponse toResponse(Genre genre);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "description", source = "description")
    Genre toEntity(CreateGenreRequest response);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "slug", source = "slug",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", source = "description",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Genre genre, UpdateGenreRequest request);
}
