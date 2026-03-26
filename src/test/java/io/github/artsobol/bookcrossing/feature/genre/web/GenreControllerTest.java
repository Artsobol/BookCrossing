package io.github.artsobol.bookcrossing.feature.genre.web;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.service.GenreService;
import io.github.artsobol.bookcrossing.infrastructure.localization.MessageService;
import io.github.artsobol.bookcrossing.infrastructure.web.error.advice.CommonControllerAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(CommonControllerAdvice.class)
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private GenreService service;

    @MockitoBean private MessageService messageService;

    @Autowired private ObjectMapper mapper;

    @Test
    void getGenres_genreExists_returns200AndBody() throws Exception {
        // given
        List<GenreResponse> response = List.of(
                new GenreResponse(1L, "Science Fiction", "Description", "science-fiction"),
                new GenreResponse(2L, "Fantasy", "Description", "fantasy")
        );
        when(service.findAll()).thenReturn(response);

        // when + then
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getFirst().id()))
                .andExpect(jsonPath("$[0].title").value(response.getFirst().title()))
                .andExpect(jsonPath("$[0].description").value(response.getFirst().description()))
                .andExpect(jsonPath("$[0].slug").value(response.getFirst().slug()))
                .andExpect(jsonPath("$[1].id").value(response.getLast().id()))
                .andExpect(jsonPath("$[1].title").value(response.getLast().title()))
                .andExpect(jsonPath("$[1].description").value(response.getLast().description()))
                .andExpect(jsonPath("$[1].slug").value(response.getLast().slug()));

        verify(service).findAll();
    }

    @Test
    @DisplayName("GET /{slug}: genre exists - returns 200 and body")
    void getGenre_genreExists_returns200AndBody() throws Exception {
        // given
        String slug = "science-fiction";
        GenreResponse response = new GenreResponse(1L, "Science Fiction", "Description", slug);
        when(service.findBySlug(slug)).thenReturn(response);

        // when + then
        mockMvc.perform(get("/api/genres/{slug}", slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.slug").value(response.slug()));

        verify(service).findBySlug(slug);
    }

    @Test
    @DisplayName("GET /{slug}: genre not exists - return 404 and body")
    void getGenre_genreNotExists_returns404AndBody() throws Exception {
        // given
        String slug = "fantasy";
        when(service.findBySlug(slug)).thenThrow(new NotFoundException("genre.slug.not.found", slug));

        String message = "Genre with slug " + slug + " not found";
        when(messageService.createMessage(eq("genre.slug.not.found"), any())).thenReturn(message);

        // when + then
        mockMvc.perform(get("/api/genres/{slug}", slug))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorCode").value("genre.slug.not.found"))
                .andExpect(jsonPath("$.message").value(message));

        verify(service).findBySlug(slug);
    }

    @Test
    @DisplayName("POST: create genre - returns 201 and body")
    void createGenre_validRequest_returnsSavedGenre() throws Exception {
        // given
        String slug = "science-fiction";
        CreateGenreRequest request = new CreateGenreRequest("Science Fiction", "Description", slug);
        GenreResponse response = new GenreResponse(1L, "Science Fiction", "Description", slug);
        when(service.create(request)).thenReturn(response);

        // when
        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.slug").value(response.slug()));

        verify(service).create(request);
    }

    @Test
    @DisplayName("POST: invalid request - return 400 and body")
    void createGenre_invalidRequest_returns400AndBody() throws Exception {
        // given
        CreateGenreRequest request = new CreateGenreRequest("Title", "Desc", null);
        when(messageService.resolveValidationMessage(any())).thenReturn("Slug must not be blank");
        when(messageService.createMessage(eq("validation.error"), any())).thenReturn("Validation error");

        // when + then
        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.path").value("/api/genres"))
                .andExpect(jsonPath("$.errors[0].field").value("slug"))
                .andExpect(jsonPath("$.errors[0].message").value("Slug must not be blank"));

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("POST: slug already exists - return 409 and body")
    void createGenre_slugAlreadyExists_returns409AndBody() throws Exception {
        // given
        String slug = "science-fiction";
        CreateGenreRequest request = new CreateGenreRequest("Science Fiction", "Description", slug);
        String messageKey = "genre.slug.exists";
        when(service.create(request)).thenThrow(new ConflictException(messageKey));

        String message = "Genre with slug " + slug + " already exists";
        when(messageService.createMessage(eq(messageKey), any())).thenReturn(message);

        // when + then
        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode").value(messageKey))
                .andExpect(jsonPath("$.message").value(message));

        verify(service).create(request);
    }

    @Test
    @DisplayName("PATCH /{slug}: genre update - return 20 and body")
    void updateGenre_validRequest_returns200AndBody() throws Exception {
        // given
        UpdateGenreRequest request = new UpdateGenreRequest(null, "New description", null);
        GenreResponse response = new GenreResponse(1L, "Fantasy", request.description(), "fantasy");
        when(service.update("fantasy", request)).thenReturn(response);

        // when + then
        mockMvc.perform(patch("/api/genres/fantasy").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.slug").value(response.slug()));

        verify(service).update("fantasy", request);
    }

    @Test
    @DisplayName("PATCH /{slug}: slug already exists - return 409 and body")
    void updateGenre_slugAlreadyExists_returns409AndBody() throws Exception {
        // given
        String slug = "science-fiction";
        UpdateGenreRequest request = new UpdateGenreRequest("Science Fiction", "Description", slug);
        String messageKey = "genre.slug.exists";
        when(service.update("fantasy", request)).thenThrow(new ConflictException(messageKey));

        String message = "Genre with slug " + slug + " already exists";
        when(messageService.createMessage(eq(messageKey), any())).thenReturn(message);

        // when + then
        mockMvc.perform(patch("/api/genres/fantasy").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode").value(messageKey))
                .andExpect(jsonPath("$.message").value(message));

        verify(service).update("fantasy", request);
    }

}