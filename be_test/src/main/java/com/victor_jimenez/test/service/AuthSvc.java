package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.AuthResponse;
import com.victor_jimenez.test.model.LoginRequest;
import com.victor_jimenez.test.model.SignupRequest;

public interface AuthSvc {
    AuthResponse signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
}
