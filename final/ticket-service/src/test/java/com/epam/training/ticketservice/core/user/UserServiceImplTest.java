package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.epam.training.ticketservice.core.user.persistence.entity.User.Role.ADMIN;
import static com.epam.training.ticketservice.core.user.persistence.entity.User.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserServiceImpl underTest = new UserServiceImpl(userRepository);
    
    @Test
    void testSignInPrivilegedShouldSetLoggedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        User user = new User("admin", "admin", ADMIN);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword("admin", "admin")).thenReturn(Optional.of(user));
        
        //When
        Optional<UserDto> actual = underTest.signInPrivileged("admin", "admin");
        
        //Then
        assertEquals(expected.get().getUsername(), actual.get().getUsername());
        assertEquals(expected.get().getRole(), actual.get().getRole());
        verify(userRepository).findByUsernameAndPassword("admin", "admin");
    }
    
    @Test
    void testSignInShouldSetLoggedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        User user = new User("user", "user", USER);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword("user", "user")).thenReturn(Optional.of(user));

        //When
        Optional<UserDto> actual = underTest.signIn("user", "user");

        //Then
        assertEquals(expected.get().getUsername(), actual.get().getUsername());
        assertEquals(expected.get().getRole(), actual.get().getRole());
        verify(userRepository).findByUsernameAndPassword("user", "user");
    }
    
    @Test
    void testSignInPrivilegedShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        //Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPassword("dummy", "dummy")).thenReturn(Optional.empty());
        
        //When
        Optional<UserDto> actual = underTest.signInPrivileged("dummy", "dummy");

        // Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPassword("dummy", "dummy");
    }
    
    @Test
    void testSignOutShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.signOut();

        // Then
        assertEquals(expected, actual);
    }
    
    @Test
    void testSignOutShouldReturnThePreviouslyLoggedInUserWhenThereIsALoggedInUser() {
        // Given
        User user = new User("user", "password", USER);
        when(userRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.signIn("user", "password");

        // When
        Optional<UserDto> actual = underTest.signOut();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testDescribeShouldReturnTheLoggedInUserWhenThereIsALoggedInUser() {
        // Given
        User user = new User("user", "password", USER);
        when(userRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.signIn("user", "password");

        // When
        Optional<UserDto> actual = underTest.describe();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testDescribeShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.describe();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignUpUserShouldCallUserRepositoryWhenTheInputIsValid() {
        // Given
        // When
        underTest.signUp("user", "pass");

        // Then
        verify(userRepository).save(new User("user", "pass", USER));
    }
}
