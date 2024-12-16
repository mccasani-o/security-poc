package com.ccasani.service.inf;

import com.ccasani.model.request.LoginRequest;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import com.ccasani.model.response.LoginResponse;

public interface UsuarioService {

     void registarUsuario(RegistrarUsuarioRequest request);

     LoginResponse login(LoginRequest request);
}
