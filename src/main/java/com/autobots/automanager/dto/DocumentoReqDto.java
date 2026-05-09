package com.autobots.automanager.dto;

import java.sql.Date;

import com.autobots.automanager.enumeracoes.TipoDocumento;

import lombok.Data;

@Data
public class DocumentoReqDto {
	private Date dataEmissao;
	private TipoDocumento tipo;

	private String numero;
}
