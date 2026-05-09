package com.autobots.automanager.dto;

import java.util.HashSet;
import java.util.Set;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import lombok.Data;

@Data
public class UsuarioReqDto {
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis = new HashSet<>();
    private EnderecoReqDto endereco;
    private Set<TelefoneReqDto> telefones = new HashSet<>();
    private Set<DocumentoReqDto> documentos = new HashSet<>();
    private Set<EmailReqDto> emails = new HashSet<>();
}
