package com.autobots.automanager.dto;

import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;

@Data
public class VeiculoReqDto {
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private Long proprietarioId;
}
