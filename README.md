# Internet Programmierung

<a href="https://imgur.com/N4tql8g"><img src="https://i.imgur.com/N4tql8g.png" title="source: imgur.com" height=230 /></a>

## Anleitungen zum lokalen Ausführen

* Back-End:   [Hier](/backend/README.md)
* Front-End:  [Hier](/frontend/README.md)


## Softwarekomponenten
| Komponente | Technologie | Packagemanger |
| ------ | ------ | ------ |
| Front-End | Vue.js und DevExtreme | NPM |
| Back-End | Java Springboot 1.8 | Maven |
| Datenbank | MySQL 5.7 | n/a  |


## Azure Cloud Services

| Service | Beschreibung |
| ------ | ------ |
| Storage Account | Use static site feature for Vue.js Front-End hosting |
| Web App |Use a Managed Tomcat 8.5 server for hosting the Back-End. Open {url}/swagger-ui.html to view the REST endpoints |
| MySQL | Managed MySQL Database can be used as the central data store  |

### Example of an Azure Resource group

<a href="https://imgur.com/4gPbC82"><img src="https://i.imgur.com/4gPbC82.png" title="source: imgur.com" height=150 /></a>


### Entwicklungsumgebung
- VS Code mit Java Extension Pack, Java Dependency Viewer, Debugger for Java
- Node.js
- Maven


## Links
- [Swagger Codegen](https://editor.swagger.io/)
- [Spring Initializr](https://start.spring.io/)


## Namenskonventionen

### Dokumente
- Sämtliche Dokumente (Präsentationen, Diagramme, Protokolle, etc.) werden in folgendem Format benannt: "KICKERCUP - <Dokument_Typ>"

### Code

- Variablen werden entsprechend ihrem Zweck benannt, so das jeder Entwickler diese Variablen (und deren Bedeutung) über den Namen zuordnen kann
- Methoden werden entsprechend ihrer Funktion benannt, so das jeder Entwickler diese Funktionen über den Namen zuordnen kann
- Klassen werden entsprechend ihrers Dienst benannt, so das jeder Entwickler die Funktion der Klasse über den Namen zuordnen kann
