# 1. Escolha da imagem base
FROM python:3.11-slim

LABEL authors="Gabryel"

# 1. Escolha da imagem base
FROM python:3.11-slim

# 2. Definição do diretório de trabalho dentro do container
WORKDIR /app

# 3. Copiar arquivos de dependências primeiro (para otimizar cache)
COPY requirements.txt .

# 4. Instalar dependências
RUN pip install --no-cache-dir -r requirements.txt

# 5. Copiar o restante do código da aplicação
COPY . .

# 6. Expor a porta que a aplicação vai usar
EXPOSE 5000

# 7. Definir o comando padrão para rodar a API
CMD ["python", "app.py"]
