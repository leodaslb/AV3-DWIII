package com.autobots.automanager.modelos;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dto.DocumentoResDto;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<DocumentoResDto> {

    @Override
    public void adicionarLink(List<DocumentoResDto> lista) {
        for (DocumentoResDto dto : lista) {
            long id = dto.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(DocumentoControle.class)
                            .selecionarPorId(id))
                    .withSelfRel();
            dto.add(linkProprio);
        }
    }

    
    @Override
public void adicionarLink(DocumentoResDto objeto) {
    
    Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
            .methodOn(DocumentoControle.class).selecionarPorId(objeto.getId())).withSelfRel();
    objeto.add(selfLink);


    Link listaClientes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
            .methodOn(UsuarioControle.class).obterUsuarios()).withRel("todos-clientes");
    objeto.add(listaClientes);
}
	

}