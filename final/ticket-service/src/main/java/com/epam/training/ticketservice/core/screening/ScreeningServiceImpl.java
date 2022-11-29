package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    
    @Override
    public void createScreening(ScreeningDto screeningDto) {
        if (movieRepository.findByName(screeningDto.getMovie().getName()).isEmpty()) {
            throw new IllegalArgumentException("There is no movie with this name");
        }
        if (roomRepository.findByName(screeningDto.getRoom().getName()).isEmpty()) {
            throw new IllegalArgumentException("There is no room with this name");
        }
        checkIfTheRoomIsFreeAtTheGivenDate(screeningDto);
        Screening screening = new Screening(screeningDto.getMovie(),
                screeningDto.getRoom(),
                screeningDto.getBeginningDateOfScreening());
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        Optional<Screening> optionalScreening = screeningRepository
                .findByMovieAndRoomAndBeginningDateOfScreening(screeningDto.getMovie(),
                screeningDto.getRoom(),
                screeningDto.getBeginningDateOfScreening());
        if (optionalScreening.isEmpty()) {
            throw new IllegalArgumentException("There is no such screening");
        }
        Screening screening = optionalScreening.get();
        screeningRepository.delete(screening);
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    private ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .withMovie(screening.getMovie())
                .withRoom(screening.getRoom())
                .withBeginningDateOfScreening(screening.getBeginningDateOfScreening())
                .build();
    }
    
    private void checkIfTheRoomIsFreeAtTheGivenDate(ScreeningDto screeningDto) {
        Calendar calendarOfDto = Calendar.getInstance();
        calendarOfDto.setTime(screeningDto.getBeginningDateOfScreening());
        long startOfCurrentScreening = calendarOfDto.getTime().getTime();
        calendarOfDto.add(Calendar.MINUTE, screeningDto.getMovie().getLengthInMinutes());
        long endOfCurrentScreening = calendarOfDto.getTime().getTime();
        List<Screening> screenings = screeningRepository.findByRoomName(screeningDto.getRoom().getName());
        
        for (var screening : screenings) {
            Calendar calendarOfScreenings = Calendar.getInstance();
            calendarOfScreenings.setTime(screening.getBeginningDateOfScreening());
            long startOfScreening = calendarOfScreenings.getTime().getTime();
            calendarOfScreenings.add(Calendar.MINUTE, screening.getMovie().getLengthInMinutes());
            long endOfScreening = calendarOfScreenings.getTime().getTime();
            calendarOfScreenings.add(Calendar.MINUTE, 10);
            long breakEndOfScreening = calendarOfScreenings.getTime().getTime();
            
            if ((startOfScreening <= startOfCurrentScreening && endOfScreening >= startOfCurrentScreening) 
                    || (startOfScreening <= endOfCurrentScreening && endOfScreening >= endOfCurrentScreening)) {
                throw new IllegalArgumentException("There is an overlapping screening");
            }
            
            if ((endOfScreening <= startOfCurrentScreening && breakEndOfScreening >= startOfCurrentScreening) 
                    || (endOfScreening <= endOfCurrentScreening && breakEndOfScreening >= endOfCurrentScreening)) {
                throw new IllegalArgumentException(
                        "This would start in the break period after another screening in this room");
            }
        }
    }
}
