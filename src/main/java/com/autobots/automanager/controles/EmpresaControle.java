package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.EmpresaReqDto;
import com.autobots.automanager.dto.EmpresaResDto;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.AdicionadorLinkEmpresa;
import com.autobots.automanager.services.EmpresaServico;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private EmpresaServico empresaServico;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @GetMapping("/empresas")
    public ResponseEntity<List<EmpresaResDto>> obterEmpresas() {
        List<Empresa> empresas = empresaServico.selecionarTodos();
        if (empresas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<EmpresaResDto> dto = empresaServico.toResDtoList(empresas);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResDto> obterEmpresa(@PathVariable Long id) {
        Empresa empresa = empresaServico.selecionarPorId(id);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        EmpresaResDto dto = empresaServico.toResDto(empresa);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<EmpresaResDto> criarEmpresa(@RequestBody EmpresaReqDto dtoReq) {
        Empresa empresa = empresaServico.criarEmpresa(dtoReq);
        EmpresaResDto dtoRes = empresaServico.toResDto(empresa);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EmpresaResDto> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaReqDto dtoReq) {
        Empresa empresa = empresaServico.atualizar(id, dtoReq);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        EmpresaResDto dtoRes = empresaServico.toResDto(empresa);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluirEmpresa(@PathVariable Long id) {
        empresaServico.excluir(id);
        return ResponseEntity.ok().build();
    }

    // --- Associações --
    //ASSOCIAR USUARIO
    @PostMapping("/{empresaId}/usuario/{usuarioId}")
    public ResponseEntity<Void> associarUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId) {
        empresaServico.associarUsuario(empresaId, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{empresaId}/usuario/{usuarioId}")
    public ResponseEntity<Void> desassociarUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId) {
        empresaServico.desassociarUsuario(empresaId, usuarioId);
        return ResponseEntity.ok().build();
    }

    //MERCADORIA
    @PostMapping("/{empresaId}/mercadoria/{mercadoriaId}")
    public ResponseEntity<Void> associarMercadoria(@PathVariable Long empresaId, @PathVariable Long mercadoriaId) {
        empresaServico.associarMercadoria(empresaId, mercadoriaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{empresaId}/mercadoria/{mercadoriaId}")
    public ResponseEntity<Void> desassociarMercadoria(@PathVariable Long empresaId, @PathVariable Long mercadoriaId) {
        empresaServico.desassociarMercadoria(empresaId, mercadoriaId);
        return ResponseEntity.ok().build();
    }

    //SERVICO
    @PostMapping("/{empresaId}/servico/{servicoId}")
    public ResponseEntity<Void> associarServico(@PathVariable Long empresaId, @PathVariable Long servicoId) {
        empresaServico.associarServico(empresaId, servicoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{empresaId}/servico/{servicoId}")
    public ResponseEntity<Void> desassociarServico(@PathVariable Long empresaId, @PathVariable Long servicoId) {
        empresaServico.desassociarServico(empresaId, servicoId);
        return ResponseEntity.ok().build();
    }
//venda
    @PostMapping("/{empresaId}/venda/{vendaId}")
    public ResponseEntity<Void> associarVenda(@PathVariable Long empresaId, @PathVariable Long vendaId) {
        empresaServico.associarVenda(empresaId, vendaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{empresaId}/venda/{vendaId}")
    public ResponseEntity<Void> desassociarVenda(@PathVariable Long empresaId, @PathVariable Long vendaId) {
        empresaServico.desassociarVenda(empresaId, vendaId);
        return ResponseEntity.ok().build();
    }
}
