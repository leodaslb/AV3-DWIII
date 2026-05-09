package com.autobots.automanager.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EmpresaResDto extends RepresentationModel<EmpresaResDto> {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Date cadastro;
    private EnderecoResDto endereco;
    private Set<TelefoneResDto> telefones = new HashSet<>();
    private Set<UsuarioResDto> usuarios = new HashSet<>();
    private Set<MercadoriaResDto> mercadorias = new HashSet<>();
    private Set<ServicoResDto> servicos = new HashSet<>();
    private Set<VendaResDto> vendas = new HashSet<>();
}
