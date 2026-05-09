package com.autobots.automanager.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MercadoriaReqDto {
    private Date validade;
    private Date fabricacao;
    private String nome;
    private Date Cadastro;
    private long quantidade;
    private double valor;
    private String descricao;
}
