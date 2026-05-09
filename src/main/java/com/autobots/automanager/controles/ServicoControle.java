package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.ServicoReqDto;
import com.autobots.automanager.dto.ServicoResDto;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelos.AdicionadorLinkServico;
import com.autobots.automanager.services.ServicoServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private ServicoServico servicoServico;

    @Autowired
    private AdicionadorLinkServico adicionadorLink;

    @GetMapping("/servicos")
    public ResponseEntity<List<ServicoResDto>> obterServicos() {
        List<Servico> servicos = servicoServico.selecionarTodos();
        if (servicos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ServicoResDto> dto = servicoServico.toResDtoList(servicos);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResDto> obterServico(@PathVariable Long id) {
        Servico servico = servicoServico.selecionarPorId(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        ServicoResDto dto = servicoServico.toResDto(servico);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ServicoResDto> criarServico(@RequestBody ServicoReqDto dtoReq) {
        Servico servico = servicoServico.criarServico(dtoReq);
        ServicoResDto dtoRes = servicoServico.toResDto(servico);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ServicoResDto> atualizarServico(@PathVariable Long id, @RequestBody ServicoReqDto dtoReq) {
        Servico servico = servicoServico.atualizar(id, dtoReq);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        ServicoResDto dtoRes = servicoServico.toResDto(servico);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluirServico(@PathVariable Long id) {
        servicoServico.excluir(id);
        return ResponseEntity.ok().build();
    }
}