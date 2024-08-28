FROM eclipse-temurin:22-jdk-jammy

# Instala as dependências necessárias
RUN apt-get update && apt-get upgrade -y \
  && apt-get install -y wget curl unzip libgtk-3-0 libdbus-glib-1-2 libgl1-mesa-glx ca-certificates bzip2 libxtst6 libx11-xcb-dev libxt6 libpci-dev libnss3 libasound2 xvfb maven \
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

WORKDIR /app

# Copia o pom.xml e instala as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Comando para executar a aplicação com hot reload
CMD ["mvn", "spring-boot:run"]