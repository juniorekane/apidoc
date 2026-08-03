# Projekt Spezifikationen und Ideen



## Projektidee

Eine API-Bibliothek bauen.

Die zu bauende Plattform soll es grundsätzlich machen Services zu dokumentieren und zu speichern.

### Ideensammlung / Brainstorming

- Must-have
  + API-Doku anlegen (mit endpoint, Beschreibung und tags)
  + API-Doku löschen
  + API-Doku-Anlegen Review
  + API-Doku Update
  + API-Doku-Update Review
  + Support für alle HTTP-Methoden
  + Endpoint oder komplette Doku als **deprecated** markieren
  + Tragfähige Anwendung in einem Docker Container für die Integration in einer CI/CD
  + User-Authentifizierung
- Nice-to-have
  + Endpoint mit passenden Parametern abrufen
  + File-Upload für Endpoint mit Dateien als Parameter
  + Support für API mit token
- May-be
  + Support für API mit User-Account



## Projekt Spezifikationen



### Technologieauswahl

| Technologie | Version | Nutzung                      |
| ----------- | ------- | ---------------------------- |
| Java        | 25      | Programmiersprache           |
| Spring Boot | 4.0.0   | Framework                    |
| MariaDB     | 12.2.1  | Datenbank                    |
| Docker      | latest  | Containerisierung            |
| React       | latest  | Frontend                     |
| WebJars     | latest  | Frontend-Bibliothek-Sammlung |
| Lombok      | latest  | Annotations für Boilerplate  |
| NoSQL       |         |                              |

---

### Funktionale Anforderungen

- Ein User muss eine API-Dokumentation erstellen können:
  + Basis URL angeben
  + Endpoints anlegen
  + Endpoints beschreiben
  + Parameters beschreiben
  + HTTP-Methode angeben
- API bei der Erstellung einer Dokumentation soll die angelegte Dokumentation zuerst von einem Admin validiert (gereviewt) werden
- API-Dokumentation löschen, in dem Fall wird die Doku nicht aus der Datenbank gelöscht sondern von als gelöscht markiert um Doppelungen zu vermeiden
- Endpoints sollen Status und Tags kriegen:
  + Status kann sowohl **aktiv, inaktiv** als auch **deprecated** sein, falls der Endpunkt nicht mehr unterstützt wird.
  + Tags es müssen Tags als Filter gesetzt werden, um eine Suchfunktion sowie Gruppierung zu unterstützen
- Suchfunktion: Es muss möglich sein API-Dokumentationen mit Hilfe von Tags oder Suchwörter zu filtern oder gruppieren
- Update: Endpunkte sollen bearbeitbar für eingeloggte User sein, damit Tracing garantiert wird
- Sign-in: Es soll möglich sein für User einen Account in der Anwendung zu erstellen
- Review: Ein Admin soll sich Review-Anfragen ansehen und die annehmen oder ablehnen können

----

### Nicht-Funktionale Anforderungen

+ Modularität: Die Anwendung soll eine Modulare Architektur mit klaren Schichttrennung aufweisen
+ Wartbarkeit: Es soll die Anwendung sowohl im Quellcode als auch in Form eines schriftlichen Dokument so gut dokumentiert werden, dass jeder das Software übernehmen und warten kann.
+ Tragfähigkeit: Die Anwendung soll plattformunabhängig sein und sowohl unter linux, MacOS als auch Window laufen können
+ Performanz:
  + Die Anwendung soll unter 30 Sekunden auf User-Anfragen reagieren
  + Die Unterstützung von parallel Abläufe und Interaktion mehreren user gleichzeitig ist sicherzustellen
+ Error Handling: 
  + Ein sicherer Fail Over Mechanismus  muss serverseitig implementiert werden, um Ausfälle zu vermeiden.
  + Fehler müssen nachvollziehbar behandelt und geloggt werden