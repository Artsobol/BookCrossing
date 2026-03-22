package io.github.artsobol.bookcrossing.feature.book.mapper;

import io.github.artsobol.bookcrossing.config.persistence.MapStructConfig;
import io.github.artsobol.bookcrossing.feature.author.mapper.AuthorMapper;
import io.github.artsobol.bookcrossing.feature.book.dto.BookResponse;
import io.github.artsobol.bookcrossing.feature.book.dto.CreateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.dto.UpdateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.entity.Book;
import io.github.artsobol.bookcrossing.feature.genre.mapper.GenreMapper;
import io.github.artsobol.bookcrossing.feature.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapStructConfig.class, uses = {UserMapper.class, AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title")
    @Mapping(target = "description")
    Book toEntity(CreateBookRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "title")
    @Mapping(target = "description")
    @Mapping(target = "status")
    @Mapping(target = "user")
    @Mapping(target = "author")
    @Mapping(target = "genre")
    BookResponse toResponse(Book entity);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title")
    @Mapping(target = "description")
    void update(@MappingTarget Book entity, UpdateBookRequest request);
}
