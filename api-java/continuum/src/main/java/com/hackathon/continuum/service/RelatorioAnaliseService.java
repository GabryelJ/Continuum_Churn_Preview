package com.hackathon.continuum.service;

import com.hackathon.continuum.dto.RelatorioAnaliseDTO;
import com.hackathon.continuum.entity.AnaliseChurn;
import com.hackathon.continuum.entity.ResultadoChurn;
import com.hackathon.continuum.entity.SugestaoRetencaoChurn;
import com.hackathon.continuum.repository.AnaliseChurnRepository;
import com.hackathon.continuum.repository.ResultadoChurnRepository;
import com.hackathon.continuum.repository.SugestaoRetencaoChurnRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelatorioAnaliseService {

    private final AnaliseChurnRepository analiseChurnRepository;
    private final ResultadoChurnRepository resultadoChurnRepository;
    private final SugestaoRetencaoChurnRepository sugestaoRetencaoChurnRepository;

    public RelatorioAnaliseService(AnaliseChurnRepository analiseChurnRepository, ResultadoChurnRepository resultadoChurnRepository, SugestaoRetencaoChurnRepository sugestaoRetencaoChurnRepository) {
        this.resultadoChurnRepository = resultadoChurnRepository;
        this.analiseChurnRepository = analiseChurnRepository;
        this.sugestaoRetencaoChurnRepository = sugestaoRetencaoChurnRepository;
    }

    public List<RelatorioAnaliseDTO> listarRelatorio() {
        List<ResultadoChurn> resultadoChurns = resultadoChurnRepository.findAll();

        return resultadoChurns.stream()
                .map(resultado -> {
                    Optional<AnaliseChurn> clienteOptional = analiseChurnRepository.findById(resultado.getAnaliseChurn().getId());
                    AnaliseChurn cliente = clienteOptional.orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

                    Optional<SugestaoRetencaoChurn> sugestaoRetencaoChurnOptional = sugestaoRetencaoChurnRepository.findByAnaliseChurnId(cliente.getId());
                    SugestaoRetencaoChurn sugestaoRetencaoChurn = sugestaoRetencaoChurnOptional.orElseThrow(() -> new RuntimeException("Sugestão de retenção não encontrada."));

                    return new RelatorioAnaliseDTO(
                            cliente.getNome(),
                            resultado.getProbabilidadeChurn(),
                            resultado.getRisco(),
                            sugestaoRetencaoChurn.getAcaoRetencao()
                    );
                })
                .toList();


    }
}
