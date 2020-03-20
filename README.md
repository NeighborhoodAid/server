## NeighborhoodAid Rest-Server
RestAPI server for NeighborhoodAid

### Endpoints
####Base Endpoint
    http://.../api/v1/
#### Login
    POST /login
#### Shopping list
    GET /list/:id
    POST /list  - Zum erstellen von Einkaufslisten
    POST /list/:id/claim - Um einer Liste zuzusagen
    PUT /list/:id - Zum updaten  einer Liste
    DELETE /list/:id - Zum löschen von einer Liste
