package com.santechture.api.controller;


import com.santechture.api.configuration.JwtService;
import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.service.AdminService;
import com.santechture.api.validation.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class AdminController {

    private final AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder encoder;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin")
    public ResponseEntity<GeneralResponse> login(@RequestBody LoginRequest request) throws BusinessExceptions {
        return adminService.login(request);
    }

    @PostMapping("auth/generateToken")
    public String authenticateAndGetToken(@RequestBody LoginRequest authRequest) {
        log.info("password {}", authRequest.getPassword());
        log.info("password encrypted {}", encoder.encode(authRequest.getPassword()));
        log.info("username {}", authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
