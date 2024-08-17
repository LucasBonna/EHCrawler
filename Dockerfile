FROM maven:3.9.6-eclipse-temurin-22-jammy AS build

WORKDIR /app

# Copia os arquivos de construção para o contêiner
COPY pom.xml .
COPY src ./src

# Compila o projeto, ignorando os testes
RUN mvn clean package -DskipTests

# Imagem final usando Java 22
FROM eclipse-temurin:22-jre-jammy

# Atualiza os pacotes e instala dependências
RUN apt-get update && apt-get upgrade -y \
  && apt-get install -y wget curl unzip libgtk-3-0 libdbus-glib-1-2 libgl1-mesa-glx ca-certificates bzip2 libxtst6 libx11-xcb-dev libxt6 libpci-dev libnss3 libasound2 xvfb \
  && rm -rf /var/lib/apt/lists/*

# Baixa e instala o Firefox diretamente da Mozilla
RUN wget -O /tmp/firefox.tar.bz2 "https://download.mozilla.org/?product=firefox-esr-latest&os=linux64&lang=en-US" \
  && tar -xjf /tmp/firefox.tar.bz2 -C /opt/ \
  && ln -s /opt/firefox/firefox /usr/local/bin/firefox \
  && rm /tmp/firefox.tar.bz2

# Baixa e instala o GeckoDriver 0.35
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.35.0/geckodriver-v0.35.0-linux64.tar.gz \
  && tar -xzf geckodriver-v0.35.0-linux64.tar.gz \
  && mv geckodriver /usr/local/bin/ \
  && rm geckodriver-v0.35.0-linux64.tar.gz

# Configurações do diretório de trabalho e cópia do JAR
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Criar diretório para downloads
RUN mkdir -p /tmp/downloads && chmod 777 /tmp/downloads

# Executa a aplicação Java
ENTRYPOINT ["java", "-jar", "app.jar"]