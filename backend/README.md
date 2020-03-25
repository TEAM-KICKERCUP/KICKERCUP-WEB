## 0. Installiere OpenJDK 8 (oder Java) https://adoptopenjdk.net/
## 1. Klone das Repository auf deinen lokalen Computer
## 2. Führe folgendes Command im Root-Verzeichnis vom Backend-Ordner durch:
```mvnw dependency:tree```
## 3. Führe anschließend folgendes Command im Root-Verzeichnis vom Backend-Ordner durch:
```mvnw install```
## 4. Starte die Applikation in VSCode, deiner Java IDE oder über das Maven Command:
```mvnw spring-boot:run```
## 5. Navigiere in deinem Browser zu http://localhost:8080/swagger-ui.html
## 6. (Optional) Application Properties anpassen (z.B. MySQL Connection String):
 File-Path: ```backend/src/main/resources/application.properties```