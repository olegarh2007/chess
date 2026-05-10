package com.olegator.chess.service;

import com.olegator.chess.repository.UserRepository;
import com.olegator.chess.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserDetailsImpl(
                        user.getId(),
                        user.getEmail(),
                        user.getPasswordHash(),
                        Set.of(
                                new SimpleGrantedAuthority("ROLE_" + user.getRole())
                        )
                )).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
