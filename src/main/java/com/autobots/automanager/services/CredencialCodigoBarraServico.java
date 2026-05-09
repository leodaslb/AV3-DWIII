package com.autobots.automanager.services;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.CredencialCodigoBarraReqDto;

import com.autobots.automanager.dto.CredencialCodigoBarraResDto;
import com.autobots.automanager.entidades.CredencialCodigoBarra;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;


@Service
public class CredencialCodigoBarraServico {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // conversao dto
    public CredencialCodigoBarra converterCredencialCodigoBarra(CredencialCodigoBarraReqDto dto) {
        CredencialCodigoBarra credencial = new CredencialCodigoBarra();
        credencial.setCodigo(dto.getCodigo());
        credencial.setCriacao(Calendar.getInstance().getTime());
        credencial.setInativo(false);
        return credencial;
    }

    public Set<CredencialCodigoBarra> converterSetCredenciais(Set<CredencialCodigoBarraReqDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new HashSet<>();
        }
        return dtos.stream()
                .map(this::converterCredencialCodigoBarra)
                .collect(Collectors.toSet());
    }

  
    public void atualizar(CredencialCodigoBarra credencial, CredencialCodigoBarra atualizacao) {
        if (atualizacao != null) {
            if (atualizacao.getCodigo() != 0) {
                credencial.setCodigo(atualizacao.getCodigo());
            }
           
        }
    }

    // atualizar lista
    public void atualizar(Set<CredencialCodigoBarra> credenciais, Set<CredencialCodigoBarra> atualizacoes) {
        for (CredencialCodigoBarra atualizacao : atualizacoes) {
            for (CredencialCodigoBarra credencial : credenciais) {
                if (atualizacao.getId() != null && atualizacao.getId().equals(credencial.getId())) {
                    atualizar(credencial, atualizacao);
                }
            }
        }
    }

    public CredencialCodigoBarra adicionarCredencialCodigoBarra(Long usuarioId, CredencialCodigoBarraReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));

        CredencialCodigoBarra novaCredencial = converterCredencialCodigoBarra(dto);
        usuario.getCredenciais().add(novaCredencial);
        Usuario salvo = usuarioRepositorio.save(usuario);

        // busca pelo codigo
        return salvo.getCredenciais().stream()
                .filter(e -> e instanceof CredencialCodigoBarra &&
                        ((CredencialCodigoBarra) e).getCodigo() == (dto.getCodigo()))
                .map(e -> (CredencialCodigoBarra) e)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar a credencial salva"));
    }

    public CredencialCodigoBarra selecionarCredencialCodigoBarra(Long usuarioId, Long credencialId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        // filtra por instanceof antes de acessar campos específicos
        return usuario.getCredenciais().stream()
                .filter(e -> e instanceof CredencialCodigoBarra && e.getId().equals(credencialId))
                .map(e -> (CredencialCodigoBarra) e)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credencial não encontrada para este usuario"));
    }

    public CredencialCodigoBarra editarCredencialCodigoBarra(Long usuarioId, Long credencialId, CredencialCodigoBarraReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

    
        CredencialCodigoBarra credencialAlvo = usuario.getCredenciais().stream()
                .filter(e -> e instanceof CredencialCodigoBarra && e.getId().equals(credencialId))
                .map(e -> (CredencialCodigoBarra) e)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credencial não encontrada para este usuario"));

        CredencialCodigoBarra dadosAtualizados = converterCredencialCodigoBarra(dto);
        atualizar(credencialAlvo, dadosAtualizados);

        usuarioRepositorio.save(usuario);
        return credencialAlvo;
    }

    public void deletarCredencialCodigoBarra(Long usuarioId, Long credencialId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));


        boolean removido = usuario.getCredenciais()
                .removeIf(e -> e instanceof CredencialCodigoBarra && e.getId().equals(credencialId));

        if (!removido) {
            throw new RuntimeException("Credencial não encontrada para este usuario");
        }

        usuarioRepositorio.save(usuario);
    }

    public CredencialCodigoBarraResDto toResDto(CredencialCodigoBarra credencial) {
        if (credencial == null) return null;
        CredencialCodigoBarraResDto dto = new CredencialCodigoBarraResDto();
        dto.setId(credencial.getId());
        dto.setCriacao(credencial.getCriacao());
        dto.setUltimoAcesso(credencial.getUltimoAcesso());
        dto.setInativo(credencial.isInativo());
        dto.setCodigo(credencial.getCodigo());
        
        return dto;
    }
}