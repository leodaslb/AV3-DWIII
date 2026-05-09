package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.DocumentoReqDto;
import com.autobots.automanager.dto.DocumentoResDto;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.services.DocumentoServico;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private DocumentoServico documentoServico;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;

    @GetMapping("/documentos")
        public ResponseEntity<List<DocumentoResDto>> selecionarTodos() {
        List<Documento> documentos = documentoServico.mostrarTodos();
        if (documentos.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }
        List<DocumentoResDto> dto = documentoServico.toResDtoList(documentos);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResDto> selecionarPorId(@PathVariable Long id) {
        Documento documento = documentoServico.selecionarDocumentoPorId(id);
        if (documento == null){
            return ResponseEntity.notFound().build(); 
        }
        DocumentoResDto dto = documentoServico.toResDto(documento);
       
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar/{clienteId}")
    public ResponseEntity<DocumentoResDto> cadastrar(@PathVariable Long clienteId, @RequestBody DocumentoReqDto dtoReq) {
        Documento documento = documentoServico.adicionaDocumento(clienteId, dtoReq);
        

        DocumentoResDto dtoRes = documentoServico.toResDto(documento);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{clienteId}/{documentoId}")
    public ResponseEntity<DocumentoResDto> atualizarDocumento(@PathVariable Long clienteId,
                                              @PathVariable Long documentoId,
                                              @RequestBody DocumentoReqDto dtoReq) {
        Documento documento = documentoServico.editarDocumento(clienteId, documentoId, dtoReq);
         if (documento == null){
            return ResponseEntity.notFound().build(); 
        }
        DocumentoResDto dtoRes = documentoServico.toResDto(documento);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{clienteId}/{documentoId}")
    public void deletar(@PathVariable Long clienteId, @PathVariable Long documentoId) {
        documentoServico.deletarDocumento(clienteId, documentoId);
    }
}