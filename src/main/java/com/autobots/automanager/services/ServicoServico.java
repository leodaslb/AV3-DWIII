package com.autobots.automanager.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.ServicoReqDto;
import com.autobots.automanager.dto.ServicoResDto;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@Service
public class ServicoServico {

    @Autowired
    private ServicoRepositorio servicoRepositorio;

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    private Servico converterServico(ServicoReqDto dto) {
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setValor(dto.getValor());
        servico.setDescricao(dto.getDescricao());
        return servico;
    }

    public Servico criarServico(ServicoReqDto dto) {
        return servicoRepositorio.save(converterServico(dto));
    }

    public List<Servico> selecionarTodos() {
        return servicoRepositorio.findAll();
    }

    public Servico selecionarPorId(Long id) {
        return servicoRepositorio.findById(id).orElse(null);
    }

    public Servico atualizar(Long id, ServicoReqDto atualizacao) {
        Servico servico = servicoRepositorio.findById(id).orElse(null);
        if (servico != null) {
            if (!verificador.verificar(atualizacao.getNome()))
                servico.setNome(atualizacao.getNome());
            if (!verificador.verificar(atualizacao.getDescricao()))
                servico.setDescricao(atualizacao.getDescricao());
            if (atualizacao.getValor() > 0)
                servico.setValor(atualizacao.getValor());
            return servicoRepositorio.save(servico);
        }
        return null;
    }

    public void excluir(Long id) {
        Servico servico = servicoRepositorio.findById(id).orElse(null);
        if (servico != null)
            servicoRepositorio.delete(servico);
    }

    public ServicoResDto toResDto(Servico servico) {
        if (servico == null) return null;
        ServicoResDto dto = new ServicoResDto();
        dto.setId(servico.getId());
        dto.setNome(servico.getNome());
        dto.setValor(servico.getValor());
        dto.setDescricao(servico.getDescricao());
        return dto;
    }

    public List<ServicoResDto> toResDtoList(List<Servico> servicos) {
        return servicos.stream().map(this::toResDto).collect(Collectors.toList());
    }
}