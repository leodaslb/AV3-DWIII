package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.EmailReqDto;
import com.autobots.automanager.dto.EmailResDto;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.modelos.AdicionadorLinkEmail;
import com.autobots.automanager.services.EmailServico;

@RestController
@RequestMapping("/email")
public class EmailControle {

    @Autowired
    private EmailServico emailServico;

    @Autowired
    private AdicionadorLinkEmail adicionadorLink;

    @GetMapping("/{usuarioId}/{emailId}")
    public ResponseEntity<EmailResDto> obterEmail(@PathVariable Long usuarioId, @PathVariable Long emailId) {
        try {
            Email email = emailServico.selecionarEmail(usuarioId, emailId);
            EmailResDto dto = emailServico.toResDto(email);
            
           
            adicionadorLink.adicionarLink(dto, usuarioId); 
            
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cadastrar/{usuarioId}")
    public ResponseEntity<EmailResDto> cadastrarEmail(@PathVariable Long usuarioId, @RequestBody EmailReqDto dtoReq) {
        try {
            Email email = emailServico.adicionarEmail(usuarioId, dtoReq);
            EmailResDto dtoRes = emailServico.toResDto(email);
            
            adicionadorLink.adicionarLink(dtoRes, usuarioId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
        } catch (RuntimeException e) {
            // Retorna 404 caso o usuarioId não exista no banco
            return ResponseEntity.notFound().build(); 
        }
    }

    @PutMapping("/atualizar/{usuarioId}/{emailId}")
    public ResponseEntity<EmailResDto> atualizarEmail(
            @PathVariable Long usuarioId, 
            @PathVariable Long emailId, 
            @RequestBody EmailReqDto dtoReq) {
        
        try {
            Email email = emailServico.editarEmail(usuarioId, emailId, dtoReq);
            EmailResDto dtoRes = emailServico.toResDto(email);
            
            adicionadorLink.adicionarLink(dtoRes, usuarioId);
            
            return ResponseEntity.ok(dtoRes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{usuarioId}/{emailId}")
    public ResponseEntity<Void> excluirEmail(@PathVariable Long usuarioId, @PathVariable Long emailId) {
        try {
            emailServico.deletarEmail(usuarioId, emailId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}