package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    void createScreening(ScreeningDto screeningDto);
    
    void deleteScreening(ScreeningDto screeningDto);
    
    List<ScreeningDto> getScreeningList();
    
    Screening getScreening(Movie movie, Room room, Date beginningDate);
}
