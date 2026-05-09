package com.autobots.automanager.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class EmpresaReqDto {
    private String razaoSocial;
    private String nomeFantasia;
    private EnderecoReqDto endereco;
    private Set<TelefoneReqDto> telefones = new HashSet<>();
}
