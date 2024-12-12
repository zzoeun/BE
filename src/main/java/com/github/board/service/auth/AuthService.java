package com.github.board.service.auth;

import com.github.movieproject.config.securtiy.JwtTokenProvider;
import com.github.movieproject.repository.auth.role.RoleRepository;
import com.github.movieproject.repository.auth.user.UserRepository;
import com.github.movieproject.repository.auth.user.Users;
import com.github.movieproject.service.exception.NotAcceptException;
import com.github.movieproject.service.exception.NotFoundException;
import com.github.movieproject.web.advice.ErrorCode;
import com.github.movieproject.web.dto.auth.LoginRequest;
import com.github.movieproject.web.dto.auth.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Transactional(transactionManager = "tmJpa1")
    public boolean signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String username = signUpRequest.getUserName();

        if (userRepository.existsByEmail(email)) {
            return false;
        }

        Users userPrincipal = Users.builder()
                .email(email)
                .userName(username)
                .password(passwordEncoder.encode(password))
                .role(roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new NotFoundException("ROLE_USER를 찾을 수가 없습니다.")))
                .build();

        userRepository.save(userPrincipal);
        return true;

    }

    public String login(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            Users userPrincipal = userRepository.findByEmailFetchJoin(email)
                    .orElseThrow(() -> new NotFoundException("UserPrincipal을 찾을 수 없습니다."));

            String roleName = userPrincipal.getRole().getName();
            List<String> roles = Collections.singletonList(roleName);

            String token = jwtTokenProvider.createToken(email,userPrincipal.getUserName(), roles);

            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                throw new NotAcceptException(ErrorCode.LOGIN_FAILURE);
            }

            return token;

        } catch (Exception e){
            e.printStackTrace();
            throw new NotAcceptException(ErrorCode.LOGIN_FAILURE);
        }
    }

}
