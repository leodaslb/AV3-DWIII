package com.autobots.automanager.dto;

import lombok.Data;

@Data
public class CredencialUsuarioSenhaReqDto {
    private String nomeUsuario;
    private String senha;
    private Long usuarioId;
}
