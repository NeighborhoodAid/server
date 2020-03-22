## Endpoints
#### Base Endpoint
    http://.../api/v1/
#### Health
    GET /health
#### Signup - Missing
    POST /signup
#### Login - Missing
    POST /login
#### Shopping list
    GET /list/:id - Abrufen der Liste mit :id
    POST /list  - Zum erstellen von Einkaufslisten
    POST /list/:id/claim - Um einer Liste zuzusagen
    PUT /list/:id - Zum akutalisieren einer Liste
    DELETE /list/:id - Zum löschen von einer Liste

##### Erwartes Format für Shopping List
Die Shopping List Endpunkte funktionieren soweit. Es gelten allerdings folgende Einschränkungen/Bedingungen:
###### Für die Authorisierung muss ein HEADER-Feld "Authorization" mit der UUID des anfragenden Nutzers gesendet werden.
    Authorization: 550e8400-e29b-11d4-a716-446655440000
###### Da der Benutzer Endpunkt aktuell noch nicht funktionieren gibt es eine Bypass UUID:
    550e8400-e29b-11d4-a716-446655440000
###### Nach dem Neustarten einer Anwendung sind die Nutzer noch da, die Shopping Listen allerdings nicht -> wird behoben
###### Das aktuelle Format für die Listen sieht wie folgt aus:
    {
      "id": "afe2b018-6d69-4684-a666-73344c8e5fd3",
      "creator": "550e8400-e29b-11d4-a716-446655440000",
      "claimer": UUID or null,
      "creationDateTime": Long,
      "dueDateTime": Long,
      "articles: [
        {
            "amount" : Int
            "title" : String
            "description" : String
            "done" : Boolean
        }
      ]
    }
#### Nach jedem Aurfruf wird die neue, manipulierte Liste als antwort zurückgeliefert.