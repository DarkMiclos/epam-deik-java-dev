package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDto> getMovieList();

    void updateMovie(MovieDto movieDto);
    
    void deleteMovieByName(String movieName);

    void createMovie(MovieDto movieDto);

    Movie getMovie(String movieName);
}
