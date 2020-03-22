# Endpoints
## Dev-Mode
Um den Dev-Mode zu aktivieren ist folgendes in die config/config.json hinzufügen:

      "env": {
        "dev_mode": true,
      }
      
Damit werden einige "bypass" Optionen eingeschaltet, durch welche die JWT Authentifizierung deaktiviert wird um die Endpunkte effektiv zu testen.

Im Dev-Mode wird zusätzlich ein Default-Nutzer mit der Id `550e8400-e29b-11d4-a716-446655440000` angelegt. 
Dieser wird bei Deaktivierung des Dev-Modes wieder entfernt.

## JSON Datenformate
### User

    {
      "id": UUID,
      "login": {                    #Only signup/login
        "type": ("EMAIL" | GAUTH),
        "id": (E-Mail | GAuth ID)
      },
      "name": String,
      "email": String,
      "password" : String,          #Only signup/login
      "phoneNumber": String,
      "address": {
        "street": String,
        "houseNumber": String,
        "postcode": String,
        "city": String,
        "longitude": String,
        "latitude": String
      },
      "shoppingLists": [
        UUID
      ]
    }
    
### Shopping List

    {
      "id": UUID,
      "creator": UUID (User),
      "claimer": UUID (User) | null,
      "creationDateTime": Long,         #Meant for date-time in String format. Currently Epoch-Millis.
      "dueDateTime": Long,              #Meant for date-time in String format. Currently Epoch-Millis.
      "articles": [                     #Could be null or [] on miss
        {
          "amount": Int,
          "title": String,
          "description": String,
          "done": Boolean
        }
      ]
    }

#### Base Endpoint
    http://.../api/v1/
#### Health
Gibt einfache Statusinformationen zur Anwendung zurück.
- Request

        GET /health
        
- Response

        {
          "uptime": {
            "millis": 19249,
            "formatted": "00:00:19.249"
          }
        }
        

#### Signup
Registriert einen neuen Benutzer.
- Request

        POST /signup
        Body: User
        
- Response

        -> The created User
        
#### Login
Loggt einen Benutzer ein. JWT Token vorgesehen.

- Request

        POST /login
    
- Response

        -> The logged-in User
    
#### Shopping list
Alle Endpunkte zum Erstellen und Manipulieren von Einkaufslisten.

- Liste erstellen
    - Request
    
            POST /list
    
    - Response
    
            -> The created shopping list
         
- Liste abrufen
    - Request
    
            GET /list/:id
    
    - Response
    
            -> The requested shopping list
            
- Liste aktualisieren
    - Request
    
            PUT /list/:id
    
    - Response
    
            -> The modified shopping list
            
- Liste löschen
    - Request
    
            DELETE /list/:id
    
    - Response
    
            -> The deleted shopping list
            
- Einer Liste zusagen (als Einkäufer)
    - Request
    
             POST /list/:id/claim
    
    - Response
    
            -> The claimed shopping list
            
- Einkaufslisten in der Nähe finden. `:range` bezieht sich auf den geografischen Abstand (Breiten-, Längengrade).
    - Request
    
             GET /find/:range
    
    - Response
    
            [
                -> The shopping lists in "range"
            ]
           
#### Hinweise und Einschränkungen
- Alle Endpunkte für Einkaufslisten (ausgenommen `find`) erwarten zur 
Auswertung des anfragenden Nutzers die UUID des Benutzers im HTTP-Header `Authorization`:

        Authorization: UUID (User)
        
    Eine Besonderheit gilt hier für `/:id/claim`, hier wird der annehmende (claimende) Nutzer erwartet.
