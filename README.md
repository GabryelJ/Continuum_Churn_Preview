# Organizadores do Hackathon

<div align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/5/50/Oracle_logo.svg" alt="Oracle Logo" width="200"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://www.alura.com.br/assets/img/alura-logo.svg" alt="Alura Logo" width="160"/> 
  <img src="https://github.com/JoaoVenturini09/Continuum_Churn_Preview/blob/7877f973423c243bca539cc8919c29f89701eb85/favicon%20No%20Country.png" alt="NoCountry" height="80"/>
</div>


# 📊 Continuum

O Projeto Continuum propõe uma solução completa para previsão de churn, unindo Data Science e Back-end: o time de DS treina o modelo preditivo e o time de BE expõe previsões via API para que o negócio aja antes que o cliente decida sair. Com base em hábitos de uso e histórico de pagamento, a academia pode identificar clientes com alta probabilidade de evasão e realizar ações de retenção.


🎯 Desafio

O ChurnInsight busca responder à seguinte pergunta:

➡️ Quais clientes apresentam maior probabilidade de evasão ?

Essa integração permitirá que o negócio aja antes que o cliente decida sair, aumentando a retenção e reduzindo perdas.

🔎 Visão geral da arquitetura
• 	Fluxo: Dados de clientes → API Java (DTO valida e persiste em H2) → Chamada à API Python (modelo) → Resposta com probabilidade/risco → Persistência do resultado e interpretabilidade.

• 	Integração: API Java (Spring) orquestra entrada e persistência; API Python (FastAPI) entrega previsões com interpretabilidade das features mais relevantes.

• 	Persistência: Banco H2 em modo dev para agilidade e leveza; pode evoluir para RDBMS gerenciado em produção.

• 	Formatos: Integração e interoperabilidade em JSON e CSV.

---

## 🚀 Objetivos
 
- **Performance e Escalabilidade** → Otimizar processos e arquitetura para maior volume de dados e usuários.  
- **Interface e UX/UI** → Aprimorar design e usabilidade.  
- **Segurança** → Adotar boas práticas de proteção de dados.  
- **Integrações Futuras** → Planejar integrações com serviços externos relevantes.  

---

## 🔑 Variáveis consideradas 

- `nps_score`  
- `tempo_contrato_meses`  
- `tentou_cancelar_antes`  
- `valor_mensal`  
- `atrasos_pagamento_12m`  
- `duracao_media_treino_min`  
- `engajamento_por_custo`  
- `reducao_frequencia_3m`  
- `frequencia_mensal`  
- `tem_personal_trainer`  

---

## ⚙️ Tecnologias e Ferramentas

- **Python ** (microserviço e APIs)  
- **Machine Learning** → scikit-learn , pandas , numpy 
- **Banco de Dados H2** (persistência leve e integrada)  
- **Docker**  para containerização e deploy  
- **Frameworks de API** → Flask 
- **Dashboard** → Streamlit ou Dash  
- **Joblib**  para serialização de modelos  
- **Dashboard** : HTML , CSS e JavaScript
---

## 📈 Entregáveis do Projeto:

- Microserviço Python com endpoints REST  
- Modelos de machine learning integrados para previsão de churn  
- API com tratamento de erros e logs centralizados  
- Endpoint de estatísticas de dados  
- Banco H2 configurado para persistência  
- Dashboard simples para visualização de riscos e métricas  
- Processamento batch para análises periódicas  
- Container configurado para deploy  
- Parametrização de métricas de retenção  

---

## ▶️ Como executar o modelo e a API

### 1. Treinar e salvar o modelo :

Este arquivo foi Churn_Academia_V15.ipynb utilizado para criação do modelo pipeline.

```python
import joblib
from sklearn.pipeline import Pipeline

# O modelo completo e otimizado já foi treinado e está armazenado em `modelo_rf_otimizado`.
# Não precisamos recriar um pipeline, apenas salvar o existente.

joblib.dump(modelo_rf_otimizado, "modelo_pipeline_completo.pkl")

```

🔗 API Python (Flask) e integração com o modelo (Python):


```
python
from fastapi import Flask
import json

import joblib
from flask import Flask, jsonify, request

import previsao_lote


def load_config():
    with open('config.json', 'r', encoding='utf-8') as f:
        return json.load(f)

config = load_config()
VALORES_PADRAO = config['VALORES_PADRAO']
PORTA = config['PORTA']

caminho_modelo = 'modelo_pipeline_completo.pkl'


app = Flask(__name__)

model = joblib.load(caminho_modelo)


@app.route('/predict', methods=['POST'])
def predict():

    dados_dict = [request.get_json() | VALORES_PADRAO]

    try:
        resultados = previsao_lote.fazer_previsao_lote(dados_dict, model)
        print(resultados)
        resultado = resultados[0]

        return jsonify({
            "cliente_id": resultado["cliente_id"],
            "probabilidade": resultado["probabilidade_churn"],
            "risco": resultado["risco"],
            "1_mais_relevante": resultado["1_mais_relevante"],
            "2_mais_relevante": resultado["2_mais_relevante"],
            "3_mais_relevante": resultado["3_mais_relevante"]
        })

    except Exception as e:
        return jsonify({"erro": str(e)}), 400

if __name__ == '__main__':
    app.run(port=PORTA)
```

Rodar a API com arquivo conteúdo do previsao_lote.py tem a função montor para processar lista de clientes e retornar previsões com interpretabilidade:

```
import pandas as pd
import logging

def fazer_previsao_lote(lista_clientes, modelo_pipeline):
    """
    Recebe uma lista de dicionários (clientes) e o pipeline do modelo.
    Retorna uma lista de dicionários com as previsões e as 3 features mais relevantes.
    """
    try:
        df_novos = pd.DataFrame(lista_clientes)
        prob_churn = modelo_pipeline.predict_proba(df_novos)[:, 1]
        contributions = modelo_pipeline.predict_proba(df_novos)  # exemplo simplificado

        feature_names_out = df_novos.columns
        resultados = []

        for i in range(len(df_novos)):
            contrib_cliente = contributions[i]
            feat_contrib = pd.Series(contrib_cliente, index=feature_names_out)

            top_3 = feat_contrib.abs().sort_values(ascending=False).head(3).index.tolist()
            top_3_clean = [f.split('__')[-1] for f in top_3]

            resultados.append({
                'cliente_id': df_novos.iloc[i].get('cliente_id', f'cliente_{i}'),
                'probabilidade_churn': round(prob_churn[i], 4),
                'risco': 'ALTO' if prob_churn[i] >= 0.5 else 'BAIXO',
                '1_mais_relevante': top_3_clean[0] if len(top_3_clean) > 0 else None,
                '2_mais_relevante': top_3_clean[1] if len(top_3_clean) > 1 else None,
                '3_mais_relevante': top_3_clean[2] if len(top_3_clean) > 2 else None
            })

        logging.info("Resultados da previsão em lote gerados com sucesso.")
        return resultados

    except Exception as e:
        logging.error(f"Erro na função fazer_previsao_lote: {e}")
        return []


```


---

## 📡 Exemplos de Requisição e Resposta (JSON)

### Requisição


modelo de JSON para teste no docker :

{
    "nome": "Fulano",
    "cliente_id": "CLI_RISCO_1",
    "genero": "F",
    "idade": 22.0,
    "data_inicio_contrato": "10/01/2024",
    "tempo_contrato_meses": 3.0,
    "tipo_plano": "Básico",
    "valor_mensal": 89.90,
    "forma_pagamento": "Boleto",
    "frequencia_mensal": 3.0,
    "duracao_media_treino_min": 40.0,
    "tem_personal_trainer": 0.0,
    "participa_aulas_coletivas": 0.0,
    "usa_app_academia": 0.0,
    "atrasos_pagamento_12m": 1.0,
    "teve_desconto_promocao": 0.0,
    "nps_score": 2.0,
    "numero_reclamacoes": 1.0,
    "participou_eventos": 0.0,
    "reducao_frequencia_3m": 1.0,
    "dias_desde_ultimo_acesso": 20.0,
    "tentou_cancelar_antes": 1,
    "engajamento_por_custo" : "55"
}

Resposta do Json: 

{
  "probabilidade_churn": 0.7768,
  "risco": "ALTO",
  "primeiro_mais_relevante": "tentou_cancelar_antes",
  "segundo_mais_relevante": "nps_score",
  "terceiro_mais_relevante": "frequencia_mensal"
}

```
▶️ Como executar Backend Java (H2): 

• 	Pré-requisitos:

• 	JDK: Temurin/OpenJDK 17

• 	Build: Maven 3.9+

• 	Banco: H2 embutido (dev)

• 	Configuração H2 (application.properties)


```
spring.datasource.url=jdbc:h2:mem:continuumdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

DTO de entrada

Use o DTO para validar e mapear os campos recebidos pela API Java. Ele suporta aliases compatíveis com o pipeline do modelo.

```
package com.hackathon.continuum.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EntradaDTO(
    @NotNull @Min(0) @Max(10) @JsonAlias("num__nps_score") Integer nps_score,
    @NotNull @PositiveOrZero @JsonAlias("num__tempo_contrato_meses") Double tempo_contrato_meses,
    @NotBlank @JsonAlias("num__tentou_cancelar_antes") String tentou_cancelar_antes,
    @NotNull @Positive @JsonAlias("num__valor_mensal") Double valor_mensal,
    @NotNull @PositiveOrZero @JsonAlias("num__atrasos_pagamento_12m") Integer atrasos_pagamento_12m,
    @NotNull @Positive @JsonAlias("num__duracao_media_treino_min") Integer duracao_media_treino_min,
    @NotNull @PositiveOrZero @JsonAlias("num__engajamento_por_custo") Double engajamento_por_custo,
    @NotBlank @JsonAlias("num__reducao_frequencia_3m") String reducao_frequencia_3m,
    @NotNull @PositiveOrZero @JsonAlias("num__frequencia_mensal") Integer frequencia_mensal,
    @NotBlank @JsonAlias("num__tem_personal_trainer") String tem_personal_trainer,
    @Positive @JsonAlias("num__numero_reclamacoes") Integer numero_reclamacoes,
    @JsonAlias("num__participa_aulas_coletivas") String participa_aulas_coletivas,
    @JsonAlias("num__participou_eventos") String participou_eventos,
    @JsonAlias("num__uso_app_academia") String uso_app_academia,
    @JsonAlias("cat__forma_pagamento") String forma_pagamento,
    @JsonAlias("teve_desconto_promocao") String teve_desconto_promocao,
    @JsonAlias("tipo_plano") String tipo_plano,
    @JsonAlias("genero") String genero,
    @Positive @JsonAlias("idade") Integer idade,
    @JsonAlias("data_inicio_contrato") LocalDate data_inicio_contrato,
    @Positive @JsonAlias("dias_desde_ultimo_acesso") Integer dias_desde_ultimo_acesso,
    @JsonAlias("churn") String churn
) {}
```

Entidade AnalizeChurn (H2)

```
package com.hackathon.continuum.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "analize_churn")
public class AnalizeChurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cliente_id;

    @Column private String nome;

    @Column private Integer nps_score;
    @Column private Double tempo_contrato_meses;
    @Column private String tentou_cancelar_antes;
    @Column private Double valor_mensal;
    @Column private Integer atrasos_pagamento_12m;
    @Column private Integer duracao_media_treino_min;
    @Column private Double engajamento_por_custo;
    @Column private String reducao_frequencia_3m;
    @Column private Integer frequencia_mensal;
    @Column private String tem_personal_trainer;
    @Column private Integer numero_reclamacoes;
    @Column private String participa_aulas_coletivas;
    @Column private String participou_eventos;
    @Column private String uso_app_academia;
    @Column private String forma_pagamento;
    @Column private String teve_desconto_promocao;
    @Column private String tipo_plano;
    @Column private LocalDate data_inicio_contrato;
    @Column private Integer dias_desde_ultimo_acesso;
    @Column private Double churn;

    @Column private LocalDateTime criacao_data_hora;
}

```

---

## 🐳 Instalação Rápida com Docker (opcional para demo)

### Dockerfile
```dockerfile
FROM python:3.10-slim
WORKDIR /app
COPY requirements.txt .
COPY . .
RUN pip install --no-cache-dir -r requirements.txt
EXPOSE 8000
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

### requirements.txt
```
fastapi==0.103.0
uvicorn==0.23.0
scikit-learn==1.3.0
pandas==2.1.0
numpy==1.25.0
joblib==1.3.2
streamlit==1.27.0
dash==2.14.0
```

### 📦 Executando com Docker

### 1. Rodar diretamente com `docker run`
```bash
docker run -d -p 8000:8000 continuum-
---

### 2. Rodar com `docker compose`
Na raiz do projeto, execute no terminal:
```bash
docker compose up --build
```


---

## 🧪 Testando a API

Após subir o container, você pode testar no navegador ou via ferramentas como **VSCode REST Client**, **Postman** ou **cURL**:

```bash
Endpoint :

http://localhost:8080/analises-churn
```

---

## ✅ Dicas

- Certifique-se de que o **Docker Desktop** ou serviço equivalente esteja rodando.
- Caso queira alterar portas, ajuste o `docker-compose.yml`.
- Para parar os containers:
```bash
docker compose down
```
---


## 📌 Observações
     
> ⚠️ Este é o repositório oficial que será demonstrado aos responsáveis.

> As informações envolvidas são de clientes de uma empresa de Academia, utilizando **base de dados fictícia** para análise.

>  Lead-in de dados: Os aliases no DTO (JsonAlias) estão alinhados ao pipeline do modelo, facilitando integração direta.

> H2 em dev: Ideal para demonstração e testes rápidos. Em produção, migre para banco gerenciado.Interpretabilidade: As três features mais relevantes por cliente ajudam ações de retenção (marketing e suporte) de forma objetiva.

> Evolução: O projeto é modular e preparado para escalar, incluindo troca de modelo, novas variáveis e integração com serviços externos.

---

---

# 🙌 Créditos Finais — Projeto Continuum Churn Preview

Este repositório documenta o trabalho desenvolvido pela equipe **Continuum Churn Preview**, dentro da iniciativa **ChurnInsight — Prévia de Cancelamentos de Clientes**.  
O projeto uniu esforços de **Ciência de Dados** e **Back-End** para construir uma solução integrada de previsão de churn, permitindo que empresas ajam de forma preventiva na retenção de clientes.

---

## 📌 Informações da Equipe

- **Nome da equipe na plataforma:** `H12-25-B-Equipamento 31-Ciência de Dados`  
- **Nome da equipe:** `Continuum Churn Preview`  
- **Projeto:** `ChurnInsight — Prévia de Cancelamentos de Clientes`  

---

## 👨‍💻 Liderança

- **Líder Geral / Data Science:** João Venturini  
- **Líder Back-End:** Gabryel Júlio dos Santos  

---

## 👩‍🔬 Equipe de Data Science

- João Venturini — [LinkedIn](https://www.linkedin.com/in/joaoventurini/)  
- Andreza Lucas — [LinkedIn](https://www.linkedin.com/in/andreza-lucas-da-silva-datascience/)  
- João Victor Lima Caris de Oliveira — [LinkedIn](https://www.linkedin.com/in/joãovictorcybersecurity/)  
- Pedro Afonso Pinto Moraes Santos — [LinkedIn](https://www.linkedin.com/in/pedro-afonso-pinto-moraes-santos-5330621b3/)  

---

## 🖥️ Equipe de Back-End

- Nayara Calixto — [LinkedIn](https://www.linkedin.com/in/nayara-calixto-dev/)  
- Gabryel Júlio dos Santos — [LinkedIn](https://www.linkedin.com/in/gabryel-santos)  

---

## 🎯 Reconhecimento

Este projeto é fruto de **colaboração multidisciplinar**, unindo ciência de dados e engenharia de software para entregar uma solução inovadora e sustentável.  
Agradecemos a todos os membros pela dedicação, criatividade e comprometimento em cada etapa do desenvolvimento.

---
