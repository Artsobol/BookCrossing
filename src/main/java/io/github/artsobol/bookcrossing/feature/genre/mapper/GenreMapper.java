package io.github.artsobol.bookcrossing.feature.genre.mapper;

import io.github.artsobol.bookcrossing.config.persistence.MapStructConfig;
import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapStructConfig.class)
public interface GenreMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "title")
    @Mapping(target = "slug")
    @Mapping(target = "description")
    GenreResponse toResponse(Genre genre);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title")
    @Mapping(target = "slug")
    @Mapping(target = "description")
    Genre toEntity(CreateGenreRequest response);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title")
    @Mapping(target = "slug")
    @Mapping(target = "description")
    void update(@MappingTarget Genre genre, UpdateGenreRequest request);
}
