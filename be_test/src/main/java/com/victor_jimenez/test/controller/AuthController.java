package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.AuthResponse;
import com.victor_jimenez.test.model.LoginRequest;
import com.victor_jimenez.test.model.SignupRequest;
import com.victor_jimenez.test.model.UserInfoResponse;
import com.victor_jimenez.test.service.AuthSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Registro, inicio de sesion y datos del usuario autenticado")
public class AuthController {

    private final AuthSvc authSvc;

    public AuthController(AuthSvc authSvc) {
        this.authSvc = authSvc;
    }

    @PostMapping("/signup")
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una cuenta con rol USER y devuelve un token JWT.")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return new ResponseEntity<>(authSvc.signup(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica al usuario y devuelve un token JWT.")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authSvc.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Usuario autenticado", description = "Devuelve los datos del usuario dueño del token JWT enviado.")
    public ResponseEntity<UserInfoResponse> me(Authentication authentication) {
        return ResponseEntity.ok(authSvc.me(authentication.getName()));
    }
}
