# AppStore-Server

Do uruchomienia potrzeba gradle w wersji 4+  
https://gradle.org/releases/

Executable jar - [download jar](https://github.com/Patresss/AppStore-Server/raw/master/AppStore-Server.jar)
java -jar AppStore-Server.jar

**Baza dev:** http://localhost:8080/h2
```
Driver Class:	org.h2.Driver
JDBC URL:	jdbc:h2:file:./build/h2db/db/store;DB_CLOSE_DELAY=-1
User Name:	store
Password:	
```

**Socket:**
- Chat w http://localhost:8080
- Url: ws://127.0.0.1:8080/chat
- Send("/app/chat", "{\"name\":\"Patryk\", \"text\":\"Hello\"}
- Subscribe("/topic/messages")

**Rest:**
- Swagger: http://localhost:8080/swagger-ui.html
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

