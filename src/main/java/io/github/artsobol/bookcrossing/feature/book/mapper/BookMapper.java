package io.github.artsobol.bookcrossing.feature.book.mapper;

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

@Mapper(componentModel = "spring", uses = {UserMapper.class, AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "genre", source = "genre")
    BookResponse toResponse(Book entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    Book toEntity(CreateBookRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", source = "description",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Book entity, UpdateBookRequest request);
}
