package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.autobots.automanager.dto.EnderecoReqDto;
import com.autobots.automanager.dto.EnderecoResDto;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.services.EnderecoServico;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private EnderecoServico servico; 

    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;


    @GetMapping("/enderecos")
        public ResponseEntity<List<EnderecoResDto>> selecionarTodos() {
        List<Endereco> enderecos = servico.mostrarTodos();
        if(enderecos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<EnderecoResDto> dto = servico.toResDtoList(enderecos);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResDto> selecionarEndereco(@PathVariable Long id) {
        Endereco endereco = servico.selecionarEndereco(id);
        if(endereco ==null){
            return ResponseEntity.notFound().build();
        }
        EnderecoResDto dto = servico.toResDto(endereco);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastro/{clienteId}")
    public ResponseEntity<EnderecoResDto>  adicionarEndereco(@PathVariable Long clienteId, @RequestBody EnderecoReqDto dtoReq) {
        Endereco endereco = servico.adicionarEndereco(clienteId, dtoReq);
        
        EnderecoResDto dtoRes = servico.toResDto(endereco);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{clienteId}")
    public ResponseEntity<EnderecoResDto> editarEndereco(@PathVariable Long clienteId, @RequestBody EnderecoReqDto dtoReq) {
        Endereco endereco = servico.editarEndereco(clienteId, dtoReq);
        if(endereco ==null){
            return ResponseEntity.notFound().build();
        }
        EnderecoResDto dtoRes = servico.toResDto(endereco);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{clienteId}")
    public void deletarEndereco(@PathVariable Long clienteId) {
        servico.deletarEndereco(clienteId);
    }
}