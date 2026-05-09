package com.autobots.automanager.dto;



import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EnderecoResDto extends RepresentationModel<EnderecoResDto> {
	
	
	private Long id;
	
	private String estado;
	
	private String cidade;
	
	private String bairro;
	
	private String rua;
	
	private String numero;
	
	private String codigoPostal;

	private String informacoesAdicionais;

}