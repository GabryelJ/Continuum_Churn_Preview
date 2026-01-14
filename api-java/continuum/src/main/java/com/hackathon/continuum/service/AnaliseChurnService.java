package com.hackathon.continuum.service;

import org.springframework.stereotype.Service;

import com.hackathon.continuum.dto.EntradaDTO;
import com.hackathon.continuum.entity.AnaliseChurn;
import com.hackathon.continuum.repository.AnaliseChurnRepository;

@Service
public class AnaliseChurnService {

    private final AnaliseChurnRepository repository;

    public AnaliseChurnService(AnaliseChurnRepository repository) {
        this.repository = repository;
    }

    public AnaliseChurn criarAnalise(EntradaDTO dto) {

        AnaliseChurn analise = new AnaliseChurn();

        analise.getNome();
        analise.setNpsScore(dto.nps_score());
        analise.setTempoContratoMeses(dto.tempo_contrato_meses());
        analise.setTentouCancelarAntes(dto.tentou_cancelar_antes());
        analise.setValorMensal(dto.valor_mensal());
        analise.setAtrasosPagamento12m(dto.atrasos_pagamento_12m());
        analise.setDuracaoMediaTreinoMin(dto.duracao_media_treino_min());
        //analise.setEngajamentoPorCusto(dto.engajamento_por_custo());
        analise.setReducaoFrequencia3m(dto.reducao_frequencia_3m());
        analise.setFrequenciaMensal(dto.frequencia_mensal());
        analise.setTemPersonalTrainer(dto.tem_personal_trainer());
        analise.setNumeroReclamacoes(dto.numero_reclamacoes());
        analise.setParticipaAulasColetivas(dto.participa_aulas_coletivas());
        analise.setParticipouEventos(dto.participou_eventos());
        analise.setUsoAppAcademia(dto.uso_app_academia());
        analise.setFormaPagamento(dto.forma_pagamento());
        analise.setTeveDescontoPromocao(dto.teve_desconto_promocao());
        analise.setTipoPlano(dto.tipo_plano());
        analise.setDataInicioContrato(dto.data_inicio_contrato());
        analise.setDiasDesdeUltimoAcesso(dto.dias_desde_ultimo_acesso());

        return repository.save(analise);
    }
}

