package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

public interface PriceComponentService {
    void updateBasePrice(Integer amount);
    
    Integer getBasePrice();
    
    void createPriceComponent(PriceComponentDto priceComponentDto);
    
    void attachPriceComponentToRoom(String priceComponentName, String roomName);
    
    void attachPriceComponentToMovie(String priceComponentName, String movieName);
    
    void attachPriceComponentToScreening(String priceComponentName, ScreeningDto screeningDto);
    
    Integer showPriceForScreening(ScreeningDto screeningDto);
}
