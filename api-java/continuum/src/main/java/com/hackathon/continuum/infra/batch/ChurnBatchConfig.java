package com.hackathon.continuum.infra.batch;

import com.hackathon.continuum.dto.EntradaDTO;
import com.hackathon.continuum.entity.AnaliseChurn;
import com.hackathon.continuum.repository.AnaliseChurnRepository;
import com.hackathon.continuum.service.AnaliseChurnService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChurnBatchConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<EntradaDTO> reader(@Value("#{jobParameters['fullFilePath']}") String pathToFile) {
        return new FlatFileItemReaderBuilder<EntradaDTO>()
                .name("churnItemReader")
                .resource(new FileSystemResource(pathToFile))
                .delimited()
                // A ordem aqui DEVE ser exatamente a mesma do seu arquivo CSV
                .names("nome", "nps_score", "tempo_contrato_meses", "tentou_cancelar_antes", "valor_mensal",
                        "atrasos_pagamento_12m", "duracao_media_treino_min", "engajamento_por_custo",
                        "reducao_frequencia_3m", "frequencia_mensal", "tem_personal_trainer",
                        "numero_reclamacoes", "participa_aulas_coletivas", "participou_eventos",
                        "uso_app_academia", "forma_pagamento", "teve_desconto_promocao",
                        "tipo_plano", "genero", "idade", "data_inicio_contrato", "dias_desde_ultimo_acesso")
                .fieldSetMapper(fieldSet -> {
                    // Conversão manual da data do CSV (dd/MM/yyyy)
                    String dataStr = fieldSet.readString("data_inicio_contrato");
                    java.time.LocalDate dataFormatada = null;
                    if (dataStr != null && !dataStr.isEmpty()) {
                        dataFormatada = java.time.LocalDate.parse(dataStr,
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    }

                    return new EntradaDTO(
                            fieldSet.readString("nome"),
                            fieldSet.readInt("nps_score"),
                            fieldSet.readDouble("tempo_contrato_meses"),
                            fieldSet.readString("tentou_cancelar_antes"),
                            fieldSet.readDouble("valor_mensal"),
                            fieldSet.readInt("atrasos_pagamento_12m"),
                            fieldSet.readInt("duracao_media_treino_min"),
                            fieldSet.readDouble("engajamento_por_custo"),
                            fieldSet.readString("reducao_frequencia_3m"),
                            fieldSet.readInt("frequencia_mensal"),
                            fieldSet.readString("tem_personal_trainer"),
                            fieldSet.readInt("numero_reclamacoes"),
                            fieldSet.readString("participa_aulas_coletivas"),
                            fieldSet.readString("participou_eventos"),
                            fieldSet.readString("uso_app_academia"),
                            fieldSet.readString("forma_pagamento"),
                            fieldSet.readString("teve_desconto_promocao"),
                            fieldSet.readString("tipo_plano"),
                            fieldSet.readString("genero"),
                            fieldSet.readInt("idade"),
                            dataFormatada, // Data convertida corretamente
                            fieldSet.readInt("dias_desde_ultimo_acesso")
                    );
                })
                .linesToSkip(1) // Pula o cabeçalho
                .build();
    }

    @Bean
    public Step importStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           AnaliseChurnService analiseChurnService) {
        return new StepBuilder("importStep", jobRepository)
                .<EntradaDTO, AnaliseChurn>chunk(10, transactionManager) // Processa de 10 em 10
                .reader(reader(null))
                .processor(analiseChurnService::criarAnalise) // Usa seu serviço para converter
                .writer(items -> items.forEach(item -> {
                    // Aqui você pode imprimir ou salvar no banco
                    System.out.println("Batch Gravando: " + item.getNome() + item.getGenero());
                }))
                .build();
    }

    @Bean
    public Job importJob(JobRepository jobRepository, Step importStep) {
        return new JobBuilder("importJob", jobRepository)
                .start(importStep)
                .build();
    }

    @Bean
    public ItemProcessor<EntradaDTO, AnaliseChurn> processor(AnaliseChurnService service) {
        // Aqui você usa o seu serviço existente para transformar DTO em Entidade
        return service::criarAnalise; 
    }

    @Bean
    public RepositoryItemWriter<AnaliseChurn> writer(AnaliseChurnRepository repository) {
        return new RepositoryItemWriterBuilder<AnaliseChurn>()
            .repository(repository)
            .methodName("save")
            .build();
    }
}