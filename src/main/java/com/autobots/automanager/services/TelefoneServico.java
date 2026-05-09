package com.autobots.automanager.services;


import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.TelefoneReqDto;
import com.autobots.automanager.dto.TelefoneResDto;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
public class TelefoneServico {
    
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

   //conversao dto

    public Telefone converterTelefone(TelefoneReqDto dto) {
        Telefone telefone = new Telefone();
        telefone.setDdd(dto.getDdd());
        telefone.setNumero(dto.getNumero());
        return telefone;
    }

    public Set<Telefone> converterListaTelefones(Set<TelefoneReqDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new HashSet<>(); 
        }
        return dtos.stream()
                .map(this::converterTelefone)
                .collect(Collectors.toSet());
    }

  

    public void atualizar(Telefone telefone, Telefone atualizacao) {
        if (atualizacao != null) {

           if (!verificador.verificar(atualizacao.getDdd())) {
                telefone.setDdd(atualizacao.getDdd());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                telefone.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(Set<Telefone> telefones, Set<Telefone> atualizacoes) {
        for (Telefone atualizacao : atualizacoes) {
            for (Telefone telefone : telefones) {
                if (atualizacao.getId() != null) {
                  
                    if (atualizacao.getId().equals(telefone.getId())) {
                        atualizar(telefone, atualizacao);
                    }
                }
            }
        }
    }


    public Telefone adicionarTelefone(Long usuarioId, TelefoneReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        Telefone novoTelefone = converterTelefone(dto);
        usuario.getTelefones().add(novoTelefone);
        
        Usuario salvo = usuarioRepositorio.save(usuario); 
        return salvo.getTelefones().stream()
        .filter(t -> t.getNumero().equals(dto.getNumero()) && 
                     t.getDdd().equals(dto.getDdd()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Erro ao recuperar o telefone salvo"));
    }


    public Telefone selecionarTelefone(Long usuarioId, Long telefoneId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        return usuario.getTelefones().stream()
                .filter(tel -> tel.getId().equals(telefoneId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado para este usuario"));
    }


  public Telefone editarTelefone(Long usuarioId, Long telefoneId, TelefoneReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        Telefone telefoneAlvo = usuario.getTelefones().stream()
                .filter(tel -> tel.getId().equals(telefoneId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado para este usuario"));
        
        Telefone dadosAtualizados = converterTelefone(dto);
        atualizar(telefoneAlvo, dadosAtualizados);
        
        usuarioRepositorio.save(usuario);
        return telefoneAlvo;
    }


    public void deletarTelefone(Long usuarioId, Long telefoneId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        boolean removido = usuario.getTelefones().removeIf(tel -> tel.getId().equals(telefoneId));
        
        if (!removido) {
            throw new RuntimeException("Telefone não encontrado para este usuario");
        }
        
        usuarioRepositorio.save(usuario);
    }

	public TelefoneResDto toResDto(Telefone telefone) {
		if (telefone == null) return null;
		TelefoneResDto dto = new TelefoneResDto();
		dto.setId(telefone.getId());
		dto.setDdd(telefone.getDdd());
		dto.setNumero(telefone.getNumero());
		return dto;
	}
}