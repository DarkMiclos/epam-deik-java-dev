package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@Profile("!ci")
@RequiredArgsConstructor
public class PersistentDatabaseInitializer {
    private final UserRepository userRepository;
    private final PriceComponentRepository priceComponentRepository;

    @PostConstruct
    public void init() {
        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);

        Optional<PriceComponent> basePrice = priceComponentRepository.findByName("BasePrice");
        if (basePrice.isEmpty()) {
            priceComponentRepository.save(new PriceComponent(
                    "BasePrice",
                    1500
            ));
        }
    }
}
