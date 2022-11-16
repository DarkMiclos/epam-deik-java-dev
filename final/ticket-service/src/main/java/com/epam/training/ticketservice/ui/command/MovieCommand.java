package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {
    private final MovieService movieService;
    private final UserService userService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Creates a movie. Only available for admin users.")
    public MovieDto createMovie(String movieName, String genre, Integer lengthInMinutes) {
        MovieDto movieDto = MovieDto.builder()
                .withName(movieName)
                .withGenre(genre)
                .withLength(lengthInMinutes)
                .build();
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Updates an existing movie. Only available for admin users.")
    public MovieDto updateMovie(String movieName, String genre, Integer lengthInMinutes) {
        MovieDto movieDto = MovieDto.builder()
                .withName(movieName)
                .withGenre(genre)
                .withLength(lengthInMinutes)
                .build();
        movieService.updateMovie(movieDto);
        return movieDto;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Delete an existing movie. Only available for admin users.")
    public void deleteMovie(String movieName) {
        movieService.deleteMovieByName(movieName);
    }
    
    @ShellMethod(key = "list movies", value = "Lists the existing movies")
    public void listMovies() {
        List<MovieDto> movieDtoList = movieService.getMovieList();
        if(movieDtoList.isEmpty()) {
            System.out.println("There are no movies at the moment");
        }
        for (var movieDto : movieDtoList) {
            System.out.println(movieDto.getName() + " (" + movieDto.getGenre() + ", " + movieDto.getLength() + " minutes)");
        }
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().getRole() == User.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
