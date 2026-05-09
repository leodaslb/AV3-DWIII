package com.autobots.automanager.dto;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TelefoneResDto extends RepresentationModel<TelefoneResDto> {
	
	
	private Long id;
	
	private String ddd;
	
	private String numero;
}