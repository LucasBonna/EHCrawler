#!/bin/bash

# Iniciar Xvfb
Xvfb :99 -ac &

# Imprimir informações de debug
echo "Debugging information:"
echo "Java version:"
java -version
echo "Firefox version:"
firefox --version
echo "Geckodriver version:"
geckodriver --version

# Iniciar o seu aplicativo
java -jar /app/app.jar
