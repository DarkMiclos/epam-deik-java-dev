package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    
    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        Movie movie = getMovie(movieDto.getName());
        movie.setGenre(movieDto.getGenre());
        movie.setLengthInMinutes(movieDto.getLength());
        movieRepository.save(movie);
        
    }

    @Override
    public void deleteMovieByName(String movieName) {
        Movie movie = getMovie(movieName);
        movieRepository.delete(movie);
    }

    @Override
    public void createMovie(MovieDto movieDto) {
        Movie movie = new Movie(movieDto.getName(),
                movieDto.getGenre(),
                movieDto.getLength());
        movieRepository.save(movie);
    }
    
    @Override
    public Movie getMovie(String movieName) {
        Optional<Movie> optionalMovie = movieRepository.findByName(movieName);
        if (optionalMovie.isEmpty()) {
            throw new IllegalArgumentException("There is no such movie");
        } else {
            return optionalMovie.get();
        }
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .withName(movie.getName())
                .withGenre(movie.getGenre())
                .withLength(movie.getLengthInMinutes())
                .build();
    }
}
