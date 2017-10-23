# AppStore-Server

Do uruchomienia potrzeba gradle w wersji 4+  
https://gradle.org/releases/

Socket:
- Chat w http://localhost:8080
- Url: ws://127.0.0.1:8080/chat
- Send("/app/chat", "{\"name\":\"Patryk\", \"text\":\"Hello\"}
- Subscribe("/topic/messages")

Rest:
- Welcome 
    - http://localhost:8080/api/welcome
    - http://localhost:8080/api/welcome/{name}

- Game (cały CRUD)
    - http://localhost:8080/api/games


Aby uruchomić: gradle bootRun

Są dwa profile:
- dev (baza H2)
- prod (baza postgres)

Domyślnie jest dev. Aby zmienić należy w pliku build.gradle ustawić "def profiles = 'prod'". W pliku application-prod.yml jest konfiguracja do bazy postgres

