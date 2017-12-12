# AppStore-Server

## Instrukcja
Do uruchomienia potrzeba gradle w wersji 4+  
https://gradle.org/releases/

Executable jar - [download jar](https://github.com/Patresss/AppStore-Server/raw/master/AppStore-Server.jar)

Aby uruchomić: ```java -jar AppStore-Server.jar```

## Baza danych
**Baza dev:** http://localhost:8080/h2
```
Driver Class:	org.h2.Driver
JDBC URL:	jdbc:h2:file:./build/h2db/db/store;DB_CLOSE_DELAY=-1
User Name:	store
Password:	
```

## Socket:
- Chat w http://localhost:8080
- Url: ws://127.0.0.1:8080/chat
- Send("/app/chat", "{\"name\":\"Patryk\", \"text\":\"Hello\"}
- Subscribe("/topic/messages")

## Rest:
Requesty można wykonać w **Swagger**ze http://localhost:8080/swagger-ui.html

### Game
#### getGame()
 - Pobiera Grę o podanym ID
 - GET /api/games/{id}
#### getAllGames()
 - Pobranie wszystkich Gier. Lista gier nie posiada plików z Grą.
 - GET /api/games
#### createGame()
- Tworzy Grę
- POST /api/games
#### updateGame()
- Aktualizuje istniejącą gre
- PUT /api/games/{id}
#### deleteGame()
- Usuwa Grę o podanym id
- DELETE /api/games/{id}
 
## Profile i gradle
Aby uruchomić: ```gradle bootRun```

Są dwa profile:
- dev (baza H2)
- prod (baza postgres)

Domyślnie jest dev. Aby zmienić należy w pliku build.gradle ustawić "def profiles = 'prod'". W pliku application-prod.yml jest konfiguracja do bazy postgres

