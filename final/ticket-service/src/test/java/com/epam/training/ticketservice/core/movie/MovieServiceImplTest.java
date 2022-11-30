package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MovieServiceImplTest {
    private static final Movie ENTITY = new Movie("Interstellar", "Sci-fi", 169);
    private static final MovieDto DTO = MovieDto.builder()
            .withName("Interstellar")
            .withGenre("Sci-fi")
            .withLength(169)
            .build();
    private final MovieRepository movieRepository = Mockito.mock(MovieRepository.class);
    private final MovieServiceImpl underTest = new MovieServiceImpl(movieRepository);
    
    @Test
    void testCreateMovieShouldCallMovieRepositoryWhenTheInputIsValid() {
        //Given
        //When
        underTest.createMovie(DTO);
        
        //Then
        verify(movieRepository).save(ENTITY);
    }
    
    @Test
    void testGetMovieShouldThrowIllegalArgumentExceptionWhenAMovieIsNotFound() {
        //Given
        IllegalArgumentException expected = new IllegalArgumentException("There is no such movie");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.getMovie("dummy"));
        
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testGetMovieShouldReturnMovieWhenParametersAreCorrectAndMovieExists() {
        //Given
        when(movieRepository.findByName(DTO.getName())).thenReturn(Optional.of(ENTITY));
        Movie expected = ENTITY;
        
        //When
        Movie actual = underTest.getMovie(DTO.getName());
        
        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findByName(DTO.getName());
    }
    
    @Test
    void testGetMovieListShouldReturnAllMovies() {
        //Given
        when(movieRepository.findAll()).thenReturn(List.of(ENTITY));
        List<MovieDto> expected = List.of(DTO);
        
        //When
        List<MovieDto> actual = underTest.getMovieList();
        
        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findAll();
    }
    
    @Test
    void testDeleteMovieShouldDeleteMovieIfMovieExists() {
        //Given
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));
        
        //When
        underTest.deleteMovieByName(ENTITY.getName());
        
        //Then
        verify(movieRepository).delete(ENTITY);
    }
}
