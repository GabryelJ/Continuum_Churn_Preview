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
      tipo_plano: document.getElementById("tipo_plano").value,
      forma_pagamento: document.getElementById("forma_pagamento").value,
      data_inicio_contrato: document.getElementById("data_inicio_contrato").value,
      frequencia_mensal: Number(document.getElementById("frequencia_mensal").value),
      duracao_media_treino_min: Number(document.getElementById("duracao_media_treino_min").value),
      engajamento_por_custo: Number(document.getElementById("engajamento_por_custo").value),
      dias_desde_ultimo_acesso: Number(document.getElementById("dias_desde_ultimo_acesso").value),
      participa_aulas_coletivas: document.getElementById("participa_aulas_coletivas").value,
      tem_personal_trainer: document.getElementById("tem_personal_trainer").value,
      participou_eventos: document.getElementById("participou_eventos").value,
      uso_app_academia: document.getElementById("uso_app_academia").value,
      valor_mensal: Number(document.getElementById("valor_mensal").value),
      atrasos_pagamento_12m: Number(document.getElementById("atrasos_pagamento_12m").value),
      nps_score: Number(document.getElementById("nps_score").value),
      numero_reclamacoes: Number(document.getElementById("numero_reclamacoes").value),
      tentou_cancelar_antes: document.getElementById("tentou_cancelar_antes").value,
      reducao_frequencia_3m: document.getElementById("reducao_frequencia_3m").value,
      teve_desconto_promocao: document.getElementById("teve_desconto_promocao").value,
      churn: document.getElementById("churn").value
    };

    /* ===============================
       CHAMADA PARA /predict
    =============================== */
    try {
      const response = await fetch("/predict", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        throw new Error("Erro ao processar a predição");
      }

      const data = await response.json();

      document.getElementById("probabilidade").textContent =
        (data.probabilidade_churn * 100).toFixed(2) + "%";

      document.getElementById("risco").textContent = data.risco;

      const lista = document.getElementById("relevantes");
      lista.innerHTML = "";
      lista.innerHTML += `<li>${data.primeiro_mais_relevante}</li>`;
      lista.innerHTML += `<li>${data.segundo_mais_relevante}</li>`;
      lista.innerHTML += `<li>${data.terceiro_mais_relevante}</li>`;

      adicionarDashboard(payload.nome, data.probabilidade_churn, data.risco);

    } catch (error) {
      erroMsg.textContent = "Erro ao comunicar com o servidor";
      console.error(error);
    }
  });

});

/* ===============================
   DASHBOARD
================================ */
function adicionarDashboard(nome, prob, risco) {
  const tabela = document.getElementById("dashboard");
  const tr = document.createElement("tr");

  tr.innerHTML = `
    <td>${nome}</td>
    <td>${(prob * 100).toFixed(2)}%</td>
    <td>${risco}</td>
  `;

  tabela.appendChild(tr);
}

function enviarCSV() {
  alert("Envio em lote será integrado com o backend");
}
