package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    void createScreening(ScreeningDto screeningDto);
    
    void deleteScreening(ScreeningDto screeningDto);
    
    List<ScreeningDto> getScreeningList();
}
