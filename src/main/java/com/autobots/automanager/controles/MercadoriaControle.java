package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.MercadoriaReqDto;
import com.autobots.automanager.dto.MercadoriaResDto;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.services.MercadoriaServico;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

    @Autowired
    private MercadoriaServico mercadoriaServico;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLink;

    @GetMapping("/mercadorias")
    public ResponseEntity<List<MercadoriaResDto>> obterMercadorias() {
        List<Mercadoria> mercadorias = mercadoriaServico.selecionarTodos();
        if (mercadorias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MercadoriaResDto> dto = mercadoriaServico.toResDtosList(mercadorias);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MercadoriaResDto> obterMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = mercadoriaServico.selecionarPorId(id);
        if (mercadoria == null) {
            return ResponseEntity.notFound().build();
        }
        MercadoriaResDto dto = mercadoriaServico.toResDto(mercadoria);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<MercadoriaResDto> criarMercadoria(@RequestBody MercadoriaReqDto dtoReq) {
        Mercadoria mercadoria = mercadoriaServico.criarMercadoria(dtoReq);
        MercadoriaResDto dtoRes = mercadoriaServico.toResDto(mercadoria);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MercadoriaResDto> atualizarMercadoria(@PathVariable Long id, @RequestBody MercadoriaReqDto dtoReq) {
        try {
            Mercadoria mercadoria = mercadoriaServico.atualizar(id, dtoReq);
            if (mercadoria == null) {
                return ResponseEntity.notFound().build();
            }
            MercadoriaResDto dtoRes = mercadoriaServico.toResDto(mercadoria);
            adicionadorLink.adicionarLink(dtoRes);
            return ResponseEntity.ok(dtoRes);
        } catch (RuntimeException e) {
           
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long id) {
        mercadoriaServico.excluir(id);
        return ResponseEntity.ok().build();
    }
}