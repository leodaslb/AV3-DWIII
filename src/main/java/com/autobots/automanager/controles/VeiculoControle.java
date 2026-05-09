package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.VeiculoReqDto;
import com.autobots.automanager.dto.VeiculoResDto;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.AdicionadorLinkVeiculo;
import com.autobots.automanager.services.VeiculoServico;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private VeiculoServico veiculoServico;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;

    @GetMapping("/veiculos")
    public ResponseEntity<List<VeiculoResDto>> obterVeiculos() {
        List<Veiculo> veiculos = veiculoServico.selecionarTodos();
        if (veiculos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<VeiculoResDto> dto = veiculoServico.toResDtoList(veiculos);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResDto> obterVeiculo(@PathVariable Long id) {
        Veiculo veiculo = veiculoServico.selecionarPorId(id);
        if (veiculo == null) {
            return ResponseEntity.notFound().build();
        }
        VeiculoResDto dto = veiculoServico.toResDto(veiculo);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<VeiculoResDto> criarVeiculo(@RequestBody VeiculoReqDto dtoReq) {
        Veiculo veiculo = veiculoServico.criarVeiculo(dtoReq);
        VeiculoResDto dtoRes = veiculoServico.toResDto(veiculo);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VeiculoResDto> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoReqDto dtoReq) {
        Veiculo veiculo = veiculoServico.atualizar(id, dtoReq);
        if (veiculo == null) {
            return ResponseEntity.notFound().build();
        }
        VeiculoResDto dtoRes = veiculoServico.toResDto(veiculo);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long id) {
        veiculoServico.excluir(id);
        return ResponseEntity.ok().build();
    }
}