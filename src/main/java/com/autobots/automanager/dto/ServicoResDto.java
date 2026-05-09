package com.autobots.automanager.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ServicoResDto extends RepresentationModel<ServicoResDto> {
    private Long id;
    private String nome;
    private double valor;
    private String descricao;
}
