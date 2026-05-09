package com.olegator.chess.integration;

import com.olegator.chess.annotation.IT;
import com.olegator.chess.dto.UserRegDto;
import com.olegator.chess.repository.UserRepository;
import com.olegator.chess.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@IT
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegistration() {
        UserRegDto userRegDto = new UserRegDto("olegator2007", "oleg.937.cherkasov@gmail.com", "oleg", "2007");
        userService.registerUser(userRegDto);

        var maybeUser = userRepository.findByLogin("olegator2007");
        assertTrue(maybeUser.isPresent());

        var user = maybeUser.get();
        assertEquals("olegator2007", user.getLogin());
        assertEquals("oleg.937.cherkasov@gmail.com", user.getEmail());
        assertNotNull(user.getUserTimestamp());
        assertEquals(LocalDate.now(), user.getUserTimestamp().getRegistrationDate());
        //assertEquals("2007", user.getPasswordHash());
    }
}
