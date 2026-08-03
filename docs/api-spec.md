Backend API-Spezifikation
Work in progress
Endpunkte
Basic
/api/doc/
Gibt alle gespeicherten API-Dokumentationen zurück
/api/doc/{documentId}
Gibt die API-Dokumentation mit der übereinstimmenden Id zurück

Review workflow
/api/doc/create
Zum erstellen einer API-Dokumentation
Legt eine Anfrage zum erstellen einer API-Dokumentation an
Benutzt die selben Mechanismen wie das updated
/api/doc/update
Zum updated einer API-Dokumentation
Legt eine Änderungsanfrage an!!!
PUT zum erstellen einer Änderungsanfrage
PATCH zum akzeptieren einer Anfrage (Muss autorisiert sein)
Die gesendeten Änderungen werden direkt persistiert
Zusätzlich muss die Id der Änderungsanfrage mit gesendet werden, damit diese geschlossen werden kann
/api/doc/review
Gibt alle anfragen zum erstellen oder ändern einer API zurück
/api/doc/review/{requestToReviewId}
Gibt die spezifischen Daten der Anfrage zurück

Workflow zum bearbeiten einer Dokumentation
Es existiert eine Dokumentation
Der Nutzer findet die Dokumentation über eine Liste durch den Endpunkt "/api/doc"
Der Nutzer klick auf die Dokumentation. Diese wird über den Endpunkt "/api/doc/{documentID}" gefetched und angezeigt
Der Nutzer nimmt Änderungen an dem Dokument vor und sendet eine Änderungsanfrage
Ein Administierender Nutzer sieht sich die vorgelegten Anfragen an über den Endpunkt "/api/doc/review"
Es wird eine Anfrage ausgewählt
Es werden das original Dokument und die angefragten Änderungen geladen und ein diff angezeigt
Der administrierende Nutzer geht die Änderungen an den Feldern durch und wählt aus welche Felder er akzeptieren möchte
Wurden alle Felder überprüft, bestätigt der Nutzer seine angaben
Die Angaben werden versendet und die Akzeptierten Felder in dem Original persistiert und die Anfrage somit geschlossen
