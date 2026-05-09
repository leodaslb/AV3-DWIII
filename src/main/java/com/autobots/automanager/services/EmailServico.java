package com.autobots.automanager.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.EmailReqDto;
import com.autobots.automanager.dto.EmailResDto;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
public class EmailServico {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Email converterEmail(EmailReqDto dto) {
        Email email = new Email();
        email.setEndereco(dto.getEndereco());
        return email;
    }

    public Set<Email> converterListaEmails(Set<EmailReqDto> dtos) {
        if (dtos == null || dtos.isEmpty()) return new HashSet<>();
        return dtos.stream().map(this::converterEmail).collect(Collectors.toSet());
    }

    public void atualizar(Email email, Email atualizacao) {
        if (atualizacao != null && !verificador.verificar(atualizacao.getEndereco()))
            email.setEndereco(atualizacao.getEndereco());
    }

    public void atualizar(Set<Email> emails, Set<Email> atualizacoes) {
        for (Email atualizacao : atualizacoes)
            for (Email email : emails)
                if (atualizacao.getId() != null && atualizacao.getId().equals(email.getId()))
                    atualizar(email, atualizacao);
    }

    public Email adicionarEmail(Long usuarioId, EmailReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));
        Email novoEmail = converterEmail(dto);
        usuario.getEmails().add(novoEmail);
        Usuario salvo = usuarioRepositorio.save(usuario);

        
        return salvo.getEmails().stream()
                .filter(e -> e.getEndereco().equals(dto.getEndereco()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar o email salvo"));
    }

    public Email selecionarEmail(Long usuarioId, Long emailId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return usuario.getEmails().stream()
                .filter(e -> e.getId().equals(emailId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Email não encontrado para este usuario"));
    }

    public Email editarEmail(Long usuarioId, Long emailId, EmailReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        Email emailAlvo = usuario.getEmails().stream()
                .filter(e -> e.getId().equals(emailId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Email não encontrado para este usuario"));
        atualizar(emailAlvo, converterEmail(dto));
        usuarioRepositorio.save(usuario);
        return emailAlvo;
    }

    public void deletarEmail(Long usuarioId, Long emailId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        boolean removido = usuario.getEmails().removeIf(e -> e.getId().equals(emailId));
        if (!removido) throw new RuntimeException("Email não encontrado para este usuario");
        usuarioRepositorio.save(usuario);
    }

    public EmailResDto toResDto(Email email) {
        if (email == null) return null;
        EmailResDto dto = new EmailResDto();
        dto.setId(email.getId());
        dto.setEndereco(email.getEndereco());
        return dto;
    }
}
