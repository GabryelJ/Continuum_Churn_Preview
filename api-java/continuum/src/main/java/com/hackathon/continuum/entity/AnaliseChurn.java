package com.hackathon.continuum.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "analise_churn")
public class AnaliseChurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cliente_id;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "nps_score")
    private Integer npsScore;
    @Column(name = "tempo_contrato_meses")
    private Double tempoContratoMeses;
    @Column(name = "tentou_cancelar_antes")
    private String tentouCancelarAntes;
    @Column(name = "valor_mensal")
    private Double valorMensal;
    @Column(name = "atrasos_pagamento_12m")
    private Integer atrasosPagamento12m;
    @Column(name = "duracao_media_treino_min")
    private Integer duracaoMediaTreinoMin;
    //@Column(name = "engajamento_por_custo")
    //private Double engajamentoPorCusto;
    @Column(name = "reducao_frequencia_3m")
    private String reducaoFrequencia3m;
    @Column(name = "frequencia_mensal")
    private Integer frequenciaMensal;
    @Column(name = "tem_personal_trainer")
    private String temPersonalTrainer;
    @Column(name = "numero_reclamacoes")
    private Integer numeroReclamacoes;
    @Column(name = "participa_aulas_coletivas")
    private String participaAulasColetivas;
    @Column(name = "participou_eventos")
    private String participouEventos;
    @Column(name = "uso_app_academia")
    private String usoAppAcademia;
    @Column(name = "forma_pagamento")
    private String formaPagamento;
    @Column(name = "teve_desconto_promocao")
    private String teveDescontoPromocao;
    @Column(name = "tipo_plano")
    private String tipoPlano;
    @Column(name = "idade")
    private Integer idade;
    @Column(name = "genero")
    private String genero;
    @Column(name = "data_inicio_contrato")
    private LocalDate dataInicioContrato;
    @Column(name = "dias_desde_ultimo_acesso")
    private Integer diasDesdeUltimoAcesso;
    @Column(name = "churn")
    private String churn;
    @Column(name = "criacao_data_hora", nullable = false, updatable = false)
    private LocalDateTime criacaoDataHora; // Data/hora de criação

    // Construtor padrão (OBRIGATÓRIO para JPA)
    public AnaliseChurn() {
    }

    // Data e hora automática e nome não pode ser null
    @PrePersist
    private void prePersist() {
        this.criacaoDataHora = LocalDateTime.now();

        if (this.nome == null || this.nome.isBlank()) {
        this.nome = "SEM_NOME";
        }
    }

    // Getters e Setters
    public Long getId() {
        return cliente_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNpsScore() {
        return npsScore;
    }

    public void setNpsScore(Integer npsScore) {
        this.npsScore = npsScore;
    }

    public Double getTempoContratoMeses() {
        return tempoContratoMeses;
    }

    public void setTempoContratoMeses(Double tempoContratoMeses) {
        this.tempoContratoMeses = tempoContratoMeses;
    }

    public String getTentouCancelarAntes() {
        return tentouCancelarAntes;
    }

    public void setTentouCancelarAntes(String tentouCancelarAntes) {
        this.tentouCancelarAntes = tentouCancelarAntes;
    }

    public Double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(Double valorMensal) {
        this.valorMensal = valorMensal;
    }

    public Integer getAtrasosPagamento12m() {
        return atrasosPagamento12m;
    }

    public void setAtrasosPagamento12m(Integer atrasosPagamento12m) {
        this.atrasosPagamento12m = atrasosPagamento12m;
    }

    public Integer getDuracaoMediaTreinoMin() {
        return duracaoMediaTreinoMin;
    }

    public void setDuracaoMediaTreinoMin(Integer duracaoMediaTreinoMin) {
        this.duracaoMediaTreinoMin = duracaoMediaTreinoMin;
    }

    /*public Double getEngajamentoPorCusto() {
        return engajamentoPorCusto;
    }

    public void setEngajamentoPorCusto(Double engajamentoPorCusto) {
        this.engajamentoPorCusto = engajamentoPorCusto;
    }*/

    public String getReducaoFrequencia3m() {
        return reducaoFrequencia3m;
    }

    public void setReducaoFrequencia3m(String reducaoFrequencia3m) {
        this.reducaoFrequencia3m = reducaoFrequencia3m;
    }

    public Integer getFrequenciaMensal() {
        return frequenciaMensal;
    }

    public void setFrequenciaMensal(Integer frequenciaMensal) {
        this.frequenciaMensal = frequenciaMensal;
    }

    public String getTemPersonalTrainer() {
        return temPersonalTrainer;
    }

    public void setTemPersonalTrainer(String temPersonalTrainer) {
        this.temPersonalTrainer = temPersonalTrainer;
    }

    public Integer getNumeroReclamacoes() {
        return numeroReclamacoes;
    }

    public void setNumeroReclamacoes(Integer numeroReclamacoes) {
        this.numeroReclamacoes = numeroReclamacoes;
    }

    public String getParticipaAulasColetivas() {
        return participaAulasColetivas;
    }

    public void setParticipaAulasColetivas(String participaAulasColetivas) {
        this.participaAulasColetivas = participaAulasColetivas;
    }

    public String getParticipouEventos() {
        return participouEventos;
    }

    public void setParticipouEventos(String participouEventos) {
        this.participouEventos = participouEventos;
    }

    public String getUsoAppAcademia() {
        return usoAppAcademia;
    }

    public void setUsoAppAcademia(String usoAppAcademia) {
        this.usoAppAcademia = usoAppAcademia;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getTeveDescontoPromocao() {
        return teveDescontoPromocao;
    }

    public void setTeveDescontoPromocao(String teveDescontoPromocao) {
        this.teveDescontoPromocao = teveDescontoPromocao;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(String tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getDataInicioContrato() {
        return dataInicioContrato;
    }

    public void setDataInicioContrato(LocalDate dataInicioContrato) {
        this.dataInicioContrato = dataInicioContrato;
    }

    public Integer getDiasDesdeUltimoAcesso() {
        return diasDesdeUltimoAcesso;
    }

    public void setDiasDesdeUltimoAcesso(Integer diasDesdeUltimoAcesso) {
        this.diasDesdeUltimoAcesso = diasDesdeUltimoAcesso;
    }

    public String getChurn() {
        return churn;
    }

    public void setChurn(String churn) {
        this.churn = churn;
    }

    public LocalDateTime getCriacaoDataHora() {
        return criacaoDataHora;
    }

    public void setCriacaoDataHora(LocalDateTime criacaoDataHora) {
        this.criacaoDataHora = criacaoDataHora;
    }

    

}
