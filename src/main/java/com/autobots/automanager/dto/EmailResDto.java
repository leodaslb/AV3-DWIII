package com.autobots.automanager.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EmailResDto extends RepresentationModel<EmailResDto> {
    private Long id;
    private String endereco;
}
