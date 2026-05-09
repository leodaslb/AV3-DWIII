package com.autobots.automanager.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VendaResDto extends RepresentationModel<VendaResDto> {
    private Long id;
    private Date cadastro;
    private String identificacao;
    private Long clienteId;
    private Long funcionarioId;
    private VeiculoResDto veiculo;
    private Set<MercadoriaResDto> mercadorias = new HashSet<>();
    private Set<ServicoResDto> servicos = new HashSet<>();
}
