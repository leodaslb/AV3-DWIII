package com.autobots.automanager.dto;

import java.util.Date;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class MercadoriaResDto extends RepresentationModel<MercadoriaResDto> {
    private Long id;
    private Date validade;
    private Date fabricacao;
    private Date cadastro;
    private String nome;
    private long quantidade;
    private double valor;
    private String descricao;
}
