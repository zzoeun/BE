package com.github.board.service.auth.security;

import com.github.board.repository.auth.user.UserRepository;
import com.github.board.repository.auth.user.Users;
import com.github.board.repository.auth.userDetails.CustomUserDetails;
import com.github.board.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Primary
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users userPrincipal = userRepository.findByEmailFetchJoin(email).orElseThrow(() -> new NotFoundException("email 에 해당하는 UserPrincipal가 없습니다"));

        String role = userPrincipal.getRole().getName();

        return CustomUserDetails.builder()
                .userId(userPrincipal
                        .getUserId())
                .email(userPrincipal.getEmail())
                .password(userPrincipal.getPassword())
                .userName(userPrincipal.getUserName())
                .authorities(Collections.singletonList(role))
                .build();
    }
}
