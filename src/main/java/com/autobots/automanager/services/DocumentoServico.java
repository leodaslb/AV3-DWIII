package com.autobots.automanager.services;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.autobots.automanager.dto.DocumentoReqDto;
import com.autobots.automanager.dto.DocumentoResDto;
import com.autobots.automanager.entidades.Usuario;


import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;


@Service

public class DocumentoServico {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private DocumentoRepositorio documentoRepositorio;

	public Documento converterDocumento(DocumentoReqDto dto) {
        Documento documento = new Documento();
        documento.setTipo(dto.getTipo());
        documento.setNumero(dto.getNumero());
		documento.setDataEmissao(dto.getDataEmissao());
        return documento;
    }

   
    public Set<Documento> converterListaDocumentos(Set<DocumentoReqDto> dtos) {
       
        if (dtos == null || dtos.isEmpty()) {
            return new HashSet<>();
        }

        return dtos.stream()
                .map(this::converterDocumento) 
                .collect(Collectors.toSet());
    }

	
	public void atualizar(Documento documento, Documento atualizacao) {
			if (atualizacao.getTipo() != null) {
				
				documento.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				documento.setNumero(atualizacao.getNumero());
			}
		}
	

	public void atualizar(Set<Documento> documentos, Set<Documento> atualizacoes) {
		for (Documento atualizacao : atualizacoes) {
			for (Documento documento : documentos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId().equals(documento.getId())) {
						atualizar(documento, atualizacao);
					}
				}
			}
		}
	}


public Documento adicionaDocumento(Long usuarioId, DocumentoReqDto dto) {
	Usuario usuario = usuarioRepositorio.findById(usuarioId)
	.orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
	Documento novoDocumento = converterDocumento(dto);
	usuario.getDocumentos().add(novoDocumento);
	Usuario usuarioSalvo = usuarioRepositorio.save(usuario);
	return usuarioSalvo.getDocumentos().stream()
	.filter(doc -> doc.getNumero().equals(dto.getNumero()))
	.findFirst().orElseThrow(() -> new RuntimeException("Erro ao recuperar o documento salvo"));
}

public List<Documento> mostrarTodos() {
	return documentoRepositorio.findAll();
}

public Documento selecionarDocumentoPorId(Long id) {
	Documento documento = documentoRepositorio.findById(id)
	.orElseThrow(() -> new RuntimeException("Documento não encontrado"));


	return documento;
}
public Set<Documento> selecionarTodosDocumentosPorUsuarioId(Long usuarioId) {
	Usuario usuario = usuarioRepositorio.findById(usuarioId)
	.orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
	return usuario.getDocumentos();

}


public Documento editarDocumento(Long usuarioId, Long documentoId, DocumentoReqDto dto) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        
        Documento documentoAlvo = usuario.getDocumentos().stream()
                .filter(doc -> doc.getId().equals(documentoId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Documento não encontrado para este usuario"));
        
        Documento dadosAtualizados = converterDocumento(dto);
        atualizar(documentoAlvo, dadosAtualizados);
        
        usuarioRepositorio.save(usuario);
		return documentoAlvo;
}

public void deletarDocumento(Long usuarioId, Long documentoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        boolean removido = usuario.getDocumentos().removeIf(doc -> doc.getId().equals(documentoId));
        
        if (!removido) {
            throw new RuntimeException("Documento não encontrado para este usuario");
        }
        
        usuarioRepositorio.save(usuario);
    }

	public DocumentoResDto toResDto(Documento documento) {
		if (documento == null) return null;
		DocumentoResDto dto = new DocumentoResDto();
		dto.setId(documento.getId());
		dto.setTipo(documento.getTipo());
		dto.setNumero(documento.getNumero());
		return dto;
	}
	public List<DocumentoResDto> toResDtoList(List<Documento> documentos){
		return documentos.stream().map(this::toResDto).collect(Collectors.toList());
	}

}