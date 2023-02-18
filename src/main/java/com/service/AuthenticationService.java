package com.service;

import com.configuration.CustomUserDetail;
import com.constant.Role;
import com.dto.UserDto;
import com.dto.UserResponse;
import com.entity.User;
import com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse register(UserDto userDto) {
        final User user = User.builder()
            .username(userDto.getUsername())
            .password(userDto.getPassword())
            .role(Role.USER)
            .build();
        userRepository.save(user);

        return UserResponse.builder()
            .token(jwtService.generateToken(new CustomUserDetail(user)))
            .build();
    }

    public UserResponse authenticate(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        final User user = userRepository.findByUsername(userDto.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return UserResponse.builder()
            .token(jwtService.generateToken(new CustomUserDetail(user)))
            .build();
    }
}
