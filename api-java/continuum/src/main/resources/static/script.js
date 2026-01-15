document.addEventListener("DOMContentLoaded", () => {

  /* ===============================
     CONTROLE DE ABRIR / FECHAR FORM
  =============================== */
  const toggleBtn = document.getElementById("toggleFormBtn");
  const formContainer = document.getElementById("formContainer");

  toggleBtn.addEventListener("click", () => {
    const fechado = formContainer.classList.contains("form-collapsed");

    formContainer.classList.toggle("form-collapsed", !fechado);
    formContainer.classList.toggle("form-expanded", fechado);

    toggleBtn.textContent = fechado
      ? "➖ Fechar Análise"
      : "➕ Nova Análise";
  });

  /* ===============================
     ENVIO DO FORMULÁRIO
  =============================== */
  const form = document.getElementById("churnForm");
  const erroMsg = document.getElementById("formErro");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    erroMsg.textContent = "";

    // Validação básica
    const campos = form.querySelectorAll("input[required], select[required]");
    let valido = true;

    campos.forEach(campo => {
      campo.classList.remove("campo-erro");
      if (!campo.value) {
        campo.classList.add("campo-erro");
        valido = false;
      }
    });

    if (!valido) {
      erroMsg.textContent = "Preencha todos os campos obrigatórios";
      return;
    }

    /* ===============================
       MONTA JSON PARA BACKEND
    =============================== */
    const payload = {
      nome: document.getElementById("nome").value,
      idade: document.getElementById("idade").value,
      genero: document.getElementById("genero").value,
      tipo_plano: document.getElementById("tipo_plano").value,
      forma_pagamento: document.getElementById("forma_pagamento").value,
      data_inicio_contrato: document.getElementById("data_inicio_contrato").value,
      tempo_contrato_meses: Number(document.getElementById("tempo_contrato_meses").value),
      frequencia_mensal: Number(document.getElementById("frequencia_mensal").value),
      duracao_media_treino_min: Number(document.getElementById("duracao_media_treino_min").value),
      dias_desde_ultimo_acesso: Number(document.getElementById("dias_desde_ultimo_acesso").value),
      participa_aulas_coletivas: simNaoParaNumero(document.getElementById("participa_aulas_coletivas").value),
      tem_personal_trainer: simNaoParaNumero(document.getElementById("tem_personal_trainer").value),
      participou_eventos: simNaoParaNumero(document.getElementById("participou_eventos").value),
      uso_app_academia: simNaoParaNumero(document.getElementById("uso_app_academia").value),
      valor_mensal: Number(document.getElementById("valor_mensal").value),
      atrasos_pagamento_12m: Number(document.getElementById("atrasos_pagamento_12m").value),
      nps_score: Number(document.getElementById("nps_score").value),
      numero_reclamacoes: Number(document.getElementById("numero_reclamacoes").value),
      tentou_cancelar_antes: simNaoParaNumero(document.getElementById("tentou_cancelar_antes").value),
      reducao_frequencia_3m: simNaoParaNumero(document.getElementById("reducao_frequencia_3m").value),
      teve_desconto_promocao: simNaoParaNumero(document.getElementById("teve_desconto_promocao").value)
    };

    /* ===============================
       CHAMADA PARA /predict
    =============================== */
    try {
       const response = await fetch("http://localhost:8080/analises-churn", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        throw new Error("Erro ao processar a predição");
      }

      // Recarrega os relatórios após salvar tudo no backend
      await carregarRelatorioAnalises();
      await carregarRelatorioAtributos();

      // Limpa e fecha formulário
      form.reset();
      toggleBtn.click();

    } catch (error) {
      erroMsg.textContent = "Erro ao comunicar com o servidor";
      console.error(error);
    }
  });

/* ===============================
   RELATÓRIO 1 – LISTA DE ANÁLISES
================================ */
async function carregarRelatorioAnalises() {
  const response = await fetch("/analises-churn/relatorios/analises");
  const dados = await response.json();

  const tbody = document.getElementById("tabela-analises");
  tbody.innerHTML = "";

  dados.forEach(item => {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${item.nomeCliente}</td>
      <td>${(item.probabilidadeChurn * 100).toFixed(2)}%</td>
      <td>${item.risco}</td>
      <td>${item.acaoRetencao}</td>
    `;

    tbody.appendChild(tr);
  });
}

/* ===============================
   RELATÓRIO 2 – ATRIBUTOS + CONTAGEM
================================ */
async function carregarRelatorioAtributos() {
  const response = await fetch("/analises-churn/relatorios/atributos");
  const dados = await response.json();

  const tbody = document.getElementById("tabela-atributos");
  tbody.innerHTML = "";

  dados.forEach(item => {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${item.atributo}</td>
      <td>${item.quantidade}</td>
    `;

    tbody.appendChild(tr);
  });
}

/* ===============================
   CSV
================================ */
async function enviarCSV() {
  const input = document.getElementById("csvFile");

  if (!input.files.length) {
    alert("Selecione um arquivo CSV");
    return;
  }

  const formData = new FormData();
  formData.append("file", input.files[0]);

  try {
    const response = await fetch("/predict/csv", {
      method: "POST",
      body: formData
    });

    if (!response.ok) {
      throw new Error("Erro ao enviar CSV");
    }

    alert("Arquivo CSV enviado com sucesso!");
    
    // Atualiza relatórios após processamento
    await carregarRelatorioAnalises();
    await carregarRelatorioAtributos();

  } catch (error) {
    console.error(error);
    alert("Erro ao processar o CSV");
  }
}
});

function simNaoParaNumero(valor) {
  if (!valor) return null; // campo opcional
  return valor.toLowerCase() === "sim" ? 1 : 0;
}

