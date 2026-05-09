package com.autobots.automanager.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class VendaReqDto {
    private String identificacao;
    private Long clienteId;
    private Long funcionarioId;
    private Long veiculoId;
    private Set<Long> mercadoriaIds = new HashSet<>();
    private Set<Long> servicoIds = new HashSet<>();
}
