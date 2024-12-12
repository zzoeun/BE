package com.github.board.web.controller.auth;

import com.github.board.config.securtiy.JwtTokenProvider;
import com.github.board.service.auth.AuthService;
import com.github.board.service.auth.JwtBlacklistService;
import com.github.board.web.dto.auth.LoginRequest;
import com.github.board.web.dto.auth.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping(value = "/signup")
    public ResponseEntity<String> register(@RequestBody SignUpRequest signUpRequest) {
        boolean isSuccess = authService.signUp(signUpRequest);
        return ResponseEntity.ok(isSuccess ? "회원가입 성공하였습니다." : "회원가입 실패하였습니다.");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        String token = authService.login(loginRequest);
        httpServletResponse.setHeader("Bearer_Token", token);
        return ResponseEntity.ok("로그인이 성공하였습니다.");
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("로그아웃이 성공하였습니다.");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
        }
    }
}
