package com.autobots.automanager.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.EnderecoReqDto;
import com.autobots.automanager.dto.EnderecoResDto;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@Service
public class EnderecoServico {
    
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private EnderecoRepositorio enderecoRepositorio;
    public Endereco converterEndereco(EnderecoReqDto enderecoReqDto) {
        if (enderecoReqDto == null) return null;

        Endereco end = new Endereco();
        end.setEstado(enderecoReqDto.getEstado());
        end.setCidade(enderecoReqDto.getCidade());
        end.setBairro(enderecoReqDto.getBairro());
        end.setRua(enderecoReqDto.getRua());
        end.setNumero(enderecoReqDto.getNumero());
        end.setCodigoPostal(enderecoReqDto.getCodigoPostal());
        end.setInformacoesAdicionais(enderecoReqDto.getInformacoesAdicionais());
        return end;
    }

    public void atualizar(Endereco endereco, Endereco atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getEstado())) {
                endereco.setEstado(atualizacao.getEstado());
            }
            if (!verificador.verificar(atualizacao.getCidade())) {
                endereco.setCidade(atualizacao.getCidade());
            }
            if (!verificador.verificar(atualizacao.getBairro())) {
                endereco.setBairro(atualizacao.getBairro());
            }
            if (!verificador.verificar(atualizacao.getRua())) {
                endereco.setRua(atualizacao.getRua());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                endereco.setNumero(atualizacao.getNumero());
            }
            if (!verificador.verificar(atualizacao.getCodigoPostal())) {
                endereco.setCodigoPostal(atualizacao.getCodigoPostal());
            }
            if (!verificador.verificar(atualizacao.getInformacoesAdicionais())) {
                endereco.setInformacoesAdicionais(atualizacao.getInformacoesAdicionais());
            }
        }
    }

    public Endereco adicionarEndereco(Long usuarioId, EnderecoReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + usuarioId));
        
        Endereco novoEndereco = converterEndereco(dto);
        usuario.setEndereco(novoEndereco);
        
        Usuario salvo = usuarioRepositorio.save(usuario);
        return salvo.getEndereco();
    }

    public List<Endereco> mostrarTodos() {
	return enderecoRepositorio.findAll();
}

    public Endereco selecionarEndereco(Long usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + usuarioId));
        
        if (usuario.getEndereco() == null) {
            throw new RuntimeException("Usuario não possui endereço cadastrado");
        }
        
        return usuario.getEndereco();
    }

    public Endereco editarEndereco(Long usuarioId, EnderecoReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + usuarioId));
        
        if (usuario.getEndereco() == null) {
            usuario.setEndereco(converterEndereco(dto));
        } else {
            Endereco dadosAtualizados = converterEndereco(dto);
            atualizar(usuario.getEndereco(), dadosAtualizados);
        }
        
        Usuario salvo = usuarioRepositorio.save(usuario);
        return salvo.getEndereco();
    }

    public void deletarEndereco(Long usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + usuarioId));
        
        usuario.setEndereco(null);
        usuarioRepositorio.save(usuario);
    }

    public EnderecoResDto toResDto(Endereco endereco) {
        if (endereco == null) return null;
        EnderecoResDto dto = new EnderecoResDto();
        dto.setId(endereco.getId());
        dto.setEstado(endereco.getEstado());
        dto.setCidade(endereco.getCidade());
        dto.setBairro(endereco.getBairro());
        dto.setRua(endereco.getRua());
        dto.setNumero(endereco.getNumero());
        dto.setCodigoPostal(endereco.getCodigoPostal());
        dto.setInformacoesAdicionais(endereco.getInformacoesAdicionais());
        return dto;
    }
    public List<EnderecoResDto> toResDtoList(List<Endereco> enderecos){
        return enderecos.stream().map(this::toResDto).collect(Collectors.toList());
    }
}