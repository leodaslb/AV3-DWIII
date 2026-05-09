package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.TelefoneReqDto;
import com.autobots.automanager.dto.TelefoneResDto;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.services.TelefoneServico;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneServico servico;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @GetMapping("/{clienteId}/{telefoneId}")
    public ResponseEntity<TelefoneResDto> selecionarTelefone(@PathVariable Long clienteId, @PathVariable Long telefoneId) {
        Telefone telefone = servico.selecionarTelefone(clienteId, telefoneId);
        if (telefone == null) {
            return ResponseEntity.notFound().build(); 
        }
        TelefoneResDto dto = servico.toResDto(telefone);
        adicionadorLink.adicionarLink(dto, clienteId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastro/{clienteId}")
    public ResponseEntity<TelefoneResDto> adicionarTelefone(@PathVariable Long clienteId, @RequestBody TelefoneReqDto dtoReq) {
        Telefone telefone = servico.adicionarTelefone(clienteId, dtoReq);
        TelefoneResDto dtoRes = servico.toResDto(telefone);
        adicionadorLink.adicionarLink(dtoRes, clienteId);
        return  ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{clienteId}/{telefoneId}")
    public ResponseEntity<TelefoneResDto> editarTelefone(@PathVariable Long clienteId, @PathVariable Long telefoneId, @RequestBody TelefoneReqDto dtoReq) {
        Telefone telefone = servico.editarTelefone(clienteId, telefoneId, dtoReq);
         if (telefone == null) {
            return ResponseEntity.notFound().build(); 
        }
        TelefoneResDto dtoRes = servico.toResDto(telefone);
        adicionadorLink.adicionarLink(dtoRes, clienteId);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{clienteId}/{telefoneId}")
    public void deletarTelefone(@PathVariable Long clienteId, @PathVariable Long telefoneId) {
        servico.deletarTelefone(clienteId, telefoneId);
    }
}