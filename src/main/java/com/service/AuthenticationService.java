package com.service;

import com.custom.CustomUserDetail;
import com.constant.Role;
import com.dto.UserDto;
import com.dto.UserResponse;
import com.entity.User;
import com.repository.UserRepository;
import com.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(UserDto userDto) {
        final User user = userRepository.findByUsername(userDto.getUsername())
            .orElse(User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .role(Role.USER)
                .createDate(DateUtil.nowAsDate())
                .build());
        userRepository.save(user);

        return UserResponse.builder()
            .token(jwtService.generateToken(new CustomUserDetail(user)))
            .build();
    }

    @Transactional
    public UserResponse authenticate(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        final User user = userRepository.findByUsername(userDto.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return UserResponse.builder()
            .token(jwtService.generateToken(new CustomUserDetail(user)))
            .build();
    }
}
