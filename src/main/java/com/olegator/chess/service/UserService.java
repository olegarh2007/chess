package com.olegator.chess.service;

import com.olegator.chess.dto.UserRegDto;
import com.olegator.chess.dto.UserResponseDto;
import com.olegator.chess.entity.User;
import com.olegator.chess.entity.UserMedia;
import com.olegator.chess.entity.UserTimestamp;
import com.olegator.chess.exception.UserAlreadyExistsException;
import com.olegator.chess.mapper.ChatMapper;
import com.olegator.chess.mapper.UserPageMapper;
import com.olegator.chess.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository repository;
    private final UserPageMapper userPageMapper;
    private final ChatMapper chatMapper;

    public Optional<UserResponseDto> getUserPublicProfile(Long id) {
        var maybeUser = repository.findForPageById(id);
        return maybeUser.map(userPageMapper::map);
    }

    public boolean checkUserLogin(String login, String password) {
        var maybeUser = repository.findByLogin(login);
        return maybeUser.map(user -> user.getPasswordHash().equals(password)).orElse(false);
    }

    public void registerUser(UserRegDto userRegDto) {
        if (repository.existsByLogin(userRegDto.login())) {
            throw new UserAlreadyExistsException("User with login " + userRegDto.login() + " already exists");
        }
        if (repository.existsByEmail(userRegDto.email())) {
            throw new UserAlreadyExistsException("User with email " + userRegDto.email() + " already exists");
        }
        User user = new User();
        user.setLogin(userRegDto.login());
        user.setEmail(userRegDto.email());
        user.setPasswordHash(userRegDto.password());
        var userMedia = new UserMedia();
        userMedia.setAvatarUrl("https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-High-Quality-Image.png");
        userMedia.setUser(user);
        userMedia.setBackgroundUrl("https://www.pngall.com/wp-content/uploads/5/User-Profile-PNG-High-Quality-Image.png");
        user.setUserMedia(userMedia);
        var regTimestamp = new UserTimestamp();
        regTimestamp.setUser(user);
        regTimestamp.setLastLoginTime(LocalDateTime.now());
        regTimestamp.setRegistrationDate(LocalDate.now());
        user.setUserTimestamp(regTimestamp);
        repository.save(user);
    }
}
