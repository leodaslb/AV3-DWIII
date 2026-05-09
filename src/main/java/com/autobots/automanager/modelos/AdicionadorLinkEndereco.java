package com.autobots.automanager.modelos;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dto.EnderecoResDto;

@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<EnderecoResDto> {

    @Override
    public void adicionarLink(List<EnderecoResDto> lista) {
        for (EnderecoResDto dto : lista) {
            adicionarLink(dto);
        }
    }

    @Override
    public void adicionarLink(EnderecoResDto objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EnderecoControle.class)
                        .selecionarEndereco(objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);
		Link listaClientes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
            .methodOn(UsuarioControle.class).obterUsuarios())
			.withRel("todos-clientes");
    		objeto.add(listaClientes);
    }
	
	
		}
		