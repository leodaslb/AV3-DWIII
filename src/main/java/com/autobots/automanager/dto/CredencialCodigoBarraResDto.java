package com.autobots.automanager.dto;

import java.util.Date;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class CredencialCodigoBarraResDto extends RepresentationModel<CredencialCodigoBarraResDto> {
    private Long id;
    private Date criacao;
    private Date ultimoAcesso;
    private boolean inativo;
    private long codigo;
}
