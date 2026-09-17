package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.AuthResponse;
import com.victor_jimenez.test.model.LoginRequest;
import com.victor_jimenez.test.model.SignupRequest;
import com.victor_jimenez.test.model.User;
import com.victor_jimenez.test.model.UserInfoResponse;
import com.victor_jimenez.test.repository.UserRepository;
import com.victor_jimenez.test.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
class AuthSvcImpl implements AuthSvc {

    private static final Logger log = LoggerFactory.getLogger(AuthSvcImpl.class);
    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthSvcImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya esta registrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(DEFAULT_ROLE);
        user.setActive(true);
        userRepository.save(user);
        log.info("Usuario registrado: username={}, userId={}", user.getUsername(), user.getId());

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException ex) {
            log.warn("Intento de login fallido para username={}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contrasena invalidos");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contrasena invalidos"));

        log.info("Login exitoso: username={}, userId={}", user.getUsername(), user.getId());
        return buildAuthResponse(user);
    }

    @Override
    public UserInfoResponse me(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return new UserInfoResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new AuthResponse(token, "Bearer", jwtService.getTtlSeconds());
    }
}
