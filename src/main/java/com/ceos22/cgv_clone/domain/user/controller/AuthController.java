package com.ceos22.cgv_clone.domain.user.controller;

import com.ceos22.cgv_clone.domain.user.dto.request.LoginRequestDto;
import com.ceos22.cgv_clone.domain.user.dto.request.SignupRequestDto;
import com.ceos22.cgv_clone.domain.user.dto.response.LoginResponseDto;
import com.ceos22.cgv_clone.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto request) {
        authService.signup(request.getUsername(), request.getPassword());
        return "회원가입 성공";
    }

    // 로그인
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return new LoginResponseDto(token, "Bearer");
    }
}
