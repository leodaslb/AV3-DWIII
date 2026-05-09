package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.dto.ServicoResDto;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<ServicoResDto> {

    @Override
    public void adicionarLink(List<ServicoResDto> lista) {
        for (ServicoResDto dto : lista) {
            long id = dto.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ServicoControle.class)
                            .obterServico(id))
                    .withSelfRel();
            dto.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(ServicoResDto objeto) {
        // Link para o próprio recurso (Self)
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .obterServico(objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);

        // Link para a coleção completa (Lista de serviços)
        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .obterServicos())
                .withRel("servicos");
        objeto.add(linkLista);
    }
}