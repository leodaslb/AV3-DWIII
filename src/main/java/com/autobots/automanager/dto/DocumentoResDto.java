package com.autobots.automanager.dto;



import lombok.Data;

import java.sql.Date;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.enumeracoes.TipoDocumento;

@Data
public class DocumentoResDto extends RepresentationModel<DocumentoResDto> {
	
	private Long id;
	
	private Date dataEmissao;
	private TipoDocumento tipo;


	private String numero;
}