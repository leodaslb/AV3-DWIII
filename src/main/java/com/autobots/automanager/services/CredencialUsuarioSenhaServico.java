package com.autobots.automanager.services;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.CredencialUsuarioSenhaReqDto;
import com.autobots.automanager.dto.CredencialUsuarioSenhaResDto;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;


@Service
public class CredencialUsuarioSenhaServico {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // conversao dto
    public CredencialUsuarioSenha converterCredencialUsuarioSenha(CredencialUsuarioSenhaReqDto dto) {
        CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
        credencial.setNomeUsuario(dto.getNomeUsuario());
        credencial.setSenha(dto.getSenha());
        credencial.setCriacao(Calendar.getInstance().getTime());
        credencial.setInativo(false);
        return credencial;
    }

    public Set<CredencialUsuarioSenha> converterSetCredenciais(Set<CredencialUsuarioSenhaReqDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new HashSet<>();
        }
        return dtos.stream()
                .map(this::converterCredencialUsuarioSenha)
                .collect(Collectors.toSet());
    }

    // atualizar simples
    public void atualizar(CredencialUsuarioSenha credencial, CredencialUsuarioSenha atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getNomeUsuario())) {
                credencial.setNomeUsuario(atualizacao.getNomeUsuario());
            }
            if (!verificador.verificar(atualizacao.getSenha())) {
                credencial.setSenha(atualizacao.getSenha());
            }
        }
    }

    // atualizar lista
    public void atualizar(Set<CredencialUsuarioSenha> credenciais, Set<CredencialUsuarioSenha> atualizacoes) {
        for (CredencialUsuarioSenha atualizacao : atualizacoes) {
            for (CredencialUsuarioSenha credencial : credenciais) {
                if (atualizacao.getId() != null && atualizacao.getId().equals(credencial.getId())) {
                    atualizar(credencial, atualizacao);
                }
            }
        }
    }

    public CredencialUsuarioSenha adicionarCredencialUsuarioSenha(Long usuarioId, CredencialUsuarioSenhaReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));

        CredencialUsuarioSenha novaCredencial = converterCredencialUsuarioSenha(dto);
        usuario.getCredenciais().add(novaCredencial);
        Usuario salvo = usuarioRepositorio.save(usuario);

        // busca pelo nomeUsuario
        return salvo.getCredenciais().stream()
                .filter(e -> e instanceof CredencialUsuarioSenha &&
                        ((CredencialUsuarioSenha) e).getNomeUsuario().equals(dto.getNomeUsuario()))
                .map(e -> (CredencialUsuarioSenha) e)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar a credencial salva"));
    }

    public CredencialUsuarioSenha selecionarCredencialUsuarioSenha(Long usuarioId, Long credencialId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        
        return usuario.getCredenciais().stream()
                .filter(e -> e instanceof CredencialUsuarioSenha && e.getId().equals(credencialId))
                .map(e -> (CredencialUsuarioSenha) e)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credencial não encontrada para este usuario"));
    }

    public CredencialUsuarioSenha editarCredencialUsuarioSenha(Long usuarioId, Long credencialId, CredencialUsuarioSenhaReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

      
        CredencialUsuarioSenha credencialAlvo = usuario.getCredenciais().stream()
                .filter(e -> e instanceof CredencialUsuarioSenha && e.getId().equals(credencialId))
                .map(e -> (CredencialUsuarioSenha) e)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credencial não encontrada para este usuario"));

        CredencialUsuarioSenha dadosAtualizados = converterCredencialUsuarioSenha(dto);
        atualizar(credencialAlvo, dadosAtualizados);

        usuarioRepositorio.save(usuario);
        return credencialAlvo;
    }

    public void deletarCredencialUsuarioSenha(Long usuarioId, Long credencialId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        
        boolean removido = usuario.getCredenciais()
                .removeIf(e -> e instanceof CredencialUsuarioSenha && e.getId().equals(credencialId));

        if (!removido) {
            throw new RuntimeException("Credencial não encontrada para este usuario");
        }

        usuarioRepositorio.save(usuario);
    }

    public CredencialUsuarioSenhaResDto toResDto(CredencialUsuarioSenha credencial) {
        if (credencial == null) return null;
        CredencialUsuarioSenhaResDto dto = new CredencialUsuarioSenhaResDto();
        dto.setId(credencial.getId());
        dto.setCriacao(credencial.getCriacao());
        dto.setUltimoAcesso(credencial.getUltimoAcesso());
        dto.setInativo(credencial.isInativo());
        dto.setNomeUsuario(credencial.getNomeUsuario());
     
        return dto;
    }
}