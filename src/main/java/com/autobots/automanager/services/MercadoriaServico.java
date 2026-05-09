package com.autobots.automanager.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.MercadoriaReqDto;
import com.autobots.automanager.dto.MercadoriaResDto;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@Service
public class MercadoriaServico {
    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    private Mercadoria convetMercadoria(MercadoriaReqDto dto){
        Mercadoria mercadoria = new Mercadoria();
        mercadoria.setValidade(dto.getValidade());
        mercadoria.setFabricao(dto.getFabricacao());
        mercadoria.setNome(dto.getNome());
        mercadoria.setQuantidade(dto.getQuantidade());
        mercadoria.setCadastro(dto.getCadastro());
        mercadoria.setValor(dto.getValor());
        mercadoria.setDescricao(dto.getDescricao());
        return mercadoria;
    }
    public Mercadoria criarMercadoria(MercadoriaReqDto dto){
        Mercadoria mercadoria = convetMercadoria(dto);
        return mercadoriaRepositorio.save(mercadoria);
    }
    public List<Mercadoria> selecionarTodos(){
        return mercadoriaRepositorio.findAll();
    }
    public Mercadoria selecionarPorId(Long id){
        return mercadoriaRepositorio.findById(id).orElse(null);
    }
    
    private void atualizarDados(Mercadoria mercadoria, MercadoriaReqDto atualizacao){
        if (!verificador.verificar(atualizacao.getNome())){
            mercadoria.setNome(atualizacao.getNome());
        }
        if (!verificador.verificar(atualizacao.getDescricao())){
            mercadoria.setDescricao(atualizacao.getDescricao());
        }
        if(atualizacao.getFabricacao()!= null){
            mercadoria.setFabricao(atualizacao.getFabricacao());
        }
        if(atualizacao.getValidade() != null){
            mercadoria.setValidade(atualizacao.getValidade());
        }
        if(atualizacao.getQuantidade() != 0){
            mercadoria.setQuantidade(atualizacao.getQuantidade());
        }
        if(atualizacao.getValor() != 0.00){
            mercadoria.setValor(atualizacao.getValor());
        }
    }

    public Mercadoria atualizar(Long id, MercadoriaReqDto atualizacao){
        Mercadoria mercadoria = mercadoriaRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("mercadoria não encontrada"));
        if (mercadoria != null){
            atualizarDados(mercadoria, atualizacao);
            return mercadoriaRepositorio.save(mercadoria);
        }
        return null;
    }
    public void excluir(Long id){
        Mercadoria mercadoria = mercadoriaRepositorio.findById(id).orElse(null);
        if (mercadoria != null) {
            mercadoriaRepositorio.delete(mercadoria);
        }
    }
    public MercadoriaResDto toResDto( Mercadoria mercadoria) {
        if (mercadoria == null) return null;
        MercadoriaResDto dto = new MercadoriaResDto();
        dto.setId(mercadoria.getId());
        dto.setCadastro(mercadoria.getCadastro());
        dto.setDescricao(mercadoria.getDescricao());
        dto.setFabricacao(mercadoria.getFabricao());
        dto.setNome(mercadoria.getNome());
        dto.setQuantidade(mercadoria.getQuantidade());
        dto.setValidade(mercadoria.getValidade());
        dto.setValor(mercadoria.getValor());
        dto.setCadastro(mercadoria.getCadastro());
        return dto;
    }
    public List<MercadoriaResDto> toResDtosList(List<Mercadoria> mercadoria){
        return mercadoria.stream().map(this::toResDto).collect(Collectors.toList());
    }
}

