package com.autobots.automanager.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.VeiculoReqDto;
import com.autobots.automanager.dto.VeiculoResDto;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@Service
public class VeiculoServico {

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    private Veiculo converterVeiculo(VeiculoReqDto dto) {
        Veiculo veiculo = new Veiculo();
        veiculo.setModelo(dto.getModelo());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setTipo(dto.getTipo());
        Usuario proprietario = usuarioRepositorio.findById(dto.getProprietarioId())
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        veiculo.setProprietario(proprietario);
        return veiculo;
    }

    public Veiculo criarVeiculo(VeiculoReqDto dto) {
        Veiculo veiculo = converterVeiculo(dto);
        return veiculoRepositorio.save(veiculo);
    }

    public List<Veiculo> selecionarTodos() {
        return veiculoRepositorio.findAll();
    }

    public Veiculo selecionarPorId(Long id) {
        return veiculoRepositorio.findById(id).orElse(null);
    }

    private void atualizarDados(Veiculo veiculo, VeiculoReqDto atualizacao) {
        if (!verificador.verificar(atualizacao.getModelo())) {
            veiculo.setModelo(atualizacao.getModelo());
        }
        if (!verificador.verificar(atualizacao.getPlaca())) {
            veiculo.setPlaca(atualizacao.getPlaca());
        }
        if (atualizacao.getTipo() != null) {
            veiculo.setTipo(atualizacao.getTipo()); 
        }
        if (atualizacao.getProprietarioId() != null) {
            Usuario proprietario = usuarioRepositorio.findById(atualizacao.getProprietarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
            veiculo.setProprietario(proprietario);
        }
    }

    public Veiculo atualizar(Long id, VeiculoReqDto atualizacao) {
        Veiculo veiculo = veiculoRepositorio.findById(id).orElse(null);
        if (veiculo != null) {
            atualizarDados(veiculo, atualizacao);
            return veiculoRepositorio.save(veiculo);
        }
        return null;
    }

    public void excluir(Long id) {
        Veiculo veiculo = veiculoRepositorio.findById(id).orElse(null);
        if (veiculo != null) {
            veiculoRepositorio.delete(veiculo);
        }
    }

    public VeiculoResDto toResDto(Veiculo veiculo) {
        if (veiculo == null) return null;
        VeiculoResDto dto = new VeiculoResDto();
        dto.setId(veiculo.getId());
        dto.setModelo(veiculo.getModelo());
        dto.setPlaca(veiculo.getPlaca());
        dto.setTipo(veiculo.getTipo());
        if (veiculo.getProprietario() != null) {
            dto.setProprietarioId(veiculo.getProprietario().getId());
        }
        return dto;
    }

    public List<VeiculoResDto> toResDtoList(List<Veiculo> veiculos) {
        return veiculos.stream().map(this::toResDto).collect(Collectors.toList());
    }
}