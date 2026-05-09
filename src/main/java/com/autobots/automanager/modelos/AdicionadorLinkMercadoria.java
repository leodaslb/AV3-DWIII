package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.dto.MercadoriaResDto;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<MercadoriaResDto> {

    @Override
    public void adicionarLink(List<MercadoriaResDto> lista) {
        for (MercadoriaResDto dto : lista) {
            long id = dto.getId(); 
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(MercadoriaControle.class)
                            .obterMercadoria(id))
                    .withSelfRel();
            dto.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(MercadoriaResDto objeto) {
        // Link para o próprio recurso (Self)
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaControle.class)
                        .obterMercadoria(objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);

        // Link para a coleção completa (Lista de mercadorias)
        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaControle.class)
                        .obterMercadorias())
                .withRel("mercadorias");
        objeto.add(linkLista);
    }
}