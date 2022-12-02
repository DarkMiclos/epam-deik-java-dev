package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceComponentServiceImpl implements  PriceComponentService {
    private final PriceComponentRepository priceComponentRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    
    @Override
    public void updateBasePrice(Integer amount) {
        Optional<PriceComponent> optionalPriceComponent = priceComponentRepository.findByName("BasePrice");
        if (optionalPriceComponent.isPresent()) {
            optionalPriceComponent.get().setAmount(amount);
            priceComponentRepository.save(optionalPriceComponent.get());
        }
    }

    @Override
    public Integer getBasePrice() {
        Optional<PriceComponent> optionalBasePriceComponent = priceComponentRepository.findByName("BasePrice");
        if (optionalBasePriceComponent.isPresent()) {
            return optionalBasePriceComponent.get().getAmount();
        }
        return 0;
    }

    @Override
    public void createPriceComponent(PriceComponentDto priceComponentDto) {
        PriceComponent priceComponent = new PriceComponent(
                priceComponentDto.getName(),
                priceComponentDto.getAmount());
        priceComponentRepository.save(priceComponent);
    }

    @Override
    public void attachPriceComponentToRoom(String priceComponentName, String roomName) {
        Optional<PriceComponent> optionalPriceComponent = priceComponentRepository.findByName(priceComponentName);
        Optional<Room> optionalRoom = roomRepository.findByName(roomName);
        if (optionalPriceComponent.isPresent() && optionalRoom.isPresent()) {
            optionalPriceComponent.get().getRooms().add(optionalRoom.get());
            priceComponentRepository.save(optionalPriceComponent.get());
        }
    }

    @Override
    public void attachPriceComponentToMovie(String priceComponentName, String movieName) {
        Optional<PriceComponent> optionalPriceComponent = priceComponentRepository.findByName(priceComponentName);
        Optional<Movie> optionalMovie = movieRepository.findByName(movieName);
        if (optionalPriceComponent.isPresent() && optionalMovie.isPresent()) {
            optionalPriceComponent.get().getMovies().add(optionalMovie.get());
            priceComponentRepository.save(optionalPriceComponent.get());
        }
    }

    @Override
    public void attachPriceComponentToScreening(String priceComponentName, ScreeningDto screeningDto) {
        Optional<PriceComponent> optionalPriceComponent = priceComponentRepository.findByName(priceComponentName);
        Optional<Screening> optionalScreening = 
                screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(screeningDto.getMovie(),
                screeningDto.getRoom(), screeningDto.getBeginningDateOfScreening());
        if (optionalPriceComponent.isPresent() && optionalScreening.isPresent()) {
            optionalPriceComponent.get()
                    .getScreenings().add(optionalScreening.get());
            priceComponentRepository.save(optionalPriceComponent.get());
        }
    }

    @Override
    public Integer showPriceForScreening(ScreeningDto screeningDto) {
        int amount = getBasePrice();
        Optional<PriceComponent> optionalPriceComponentForMovie = 
                priceComponentRepository.findByMovies(screeningDto.getMovie());
        Optional<PriceComponent> optionalPriceComponentForRoom =
                priceComponentRepository.findByRooms(screeningDto.getRoom());
        Optional<PriceComponent> optionalPriceComponentForScreening = priceComponentRepository.findByScreenings(
                screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(
                        screeningDto.getMovie(),
                        screeningDto.getRoom(),
                        screeningDto.getBeginningDateOfScreening()).get());
        if (optionalPriceComponentForMovie.isPresent()) {
            amount += optionalPriceComponentForMovie.get().getAmount();
        }
        if (optionalPriceComponentForRoom.isPresent()) {
            amount += optionalPriceComponentForRoom.get().getAmount();
        }
        if (optionalPriceComponentForScreening.isPresent()) {
            amount += optionalPriceComponentForScreening.get().getAmount();
        }
        return amount;
    }
}
