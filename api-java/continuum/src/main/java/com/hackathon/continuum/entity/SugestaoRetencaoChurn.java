package com.hackathon.continuum.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "sugestao_retencao")
public class SugestaoRetencaoChurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sugestaoRetencao_id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private AnaliseChurn analiseChurn;

    @ManyToOne
    @JoinColumn(name = "resultado_churn_id")
    private ResultadoChurn resultadoChurn;

    @Column(nullable = false, length = 200)
    private String acaoRetencao;

    @Column(nullable = false)
    private LocalDateTime dataHoraCriacao;

    public SugestaoRetencaoChurn() {
    }

    // Data e hora automáticas
	@PrePersist
	private void prePersist() {
    	this.dataHoraCriacao = LocalDateTime.now();
	}

    // Getters e Setters
    public AnaliseChurn getAnaliseChurn() {
        return analiseChurn;
    }

    public void setAnaliseChurn(AnaliseChurn analiseChurn) {
        this.analiseChurn = analiseChurn;
    }

    public ResultadoChurn getResultadoChurn() {
        return resultadoChurn;
    }

    public void setResultadoChurn(ResultadoChurn resultadoChurn) {
        this.resultadoChurn = resultadoChurn;
    }

    public String getAcaoRetencao() {
        return acaoRetencao;
    }

    public void setAcaoRetencao(String acaoRetencao) {
        this.acaoRetencao = acaoRetencao;
    }

    

    

}
