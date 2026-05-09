package com.autobots.automanager.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String nomeUsuario;
    private String senha;
}