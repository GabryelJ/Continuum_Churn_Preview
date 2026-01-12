package com.hackathon.continuum.controller;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/predict/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job importJob;

    public BatchController(JobLauncher jobLauncher, Job importJob) {
        this.jobLauncher = jobLauncher;
        this.importJob = importJob;
    }

    @PostMapping
    public ResponseEntity<String> uploadCsv(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            // 1. Salva o arquivo em uma pasta temporária do container Docker
            Path tempFile = Files.createTempFile("upload-", ".csv");
            arquivo.transferTo(tempFile);

            // 2. Passa o caminho absoluto como parâmetro para o Job
            JobParameters params = new JobParametersBuilder()
                    .addString("fullFilePath", tempFile.toAbsolutePath().toString())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(importJob, params);
            return ResponseEntity.ok("Processamento iniciado para o arquivo: " + tempFile.getFileName());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }
}