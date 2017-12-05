package edu.inf.store.web.rest.resources

import edu.inf.store.service.GameService
import edu.inf.store.service.dto.GameDto
import edu.inf.store.web.rest.util.HeaderUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid


/**
 * REST controller do zarządzania Grami
 */
@RestController
@RequestMapping("/api")
class GameResource(private val gameService: GameService) {

    companion object {
        private val ENTITY_NAME = "Game"
        val log: Logger = LoggerFactory.getLogger(GameResource::class.java)
    }


    /**
     * GET  /games/:id : Pobiera Grę o podanym ID
     *
     * @param id Id Gry która ma zostać zwrócona
     * @return ResponseEntity ze statusem 200 (OK) oraz Gra o podanym ID lub status 404 (Not Found)
     */
    @GetMapping("/games/{id}")
    fun getGame(@PathVariable id: Long): ResponseEntity<GameDto> {
        log.info("REST request to get Game : {}", id)
        return try {
            val game = gameService.findOne(id)
            log.debug("Found Game : {}", game)
            ResponseEntity(game, HttpStatus.OK)
        } catch (e: Exception) {
            log.warn("Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * GET  /games : Pobranie wszystkich Gier. Lista gier nie posiada plików z Grą.
     *
     * @return ResponseEntity ze statusem 200 (OK) oraz lista Gier
     */
    @GetMapping("/games")
    fun getAllGames(): ResponseEntity<List<GameDto>> {
        log.info("REST request to get all Games")
        val games = gameService.findAllWithoutFile()
        return ResponseEntity(games, HttpStatus.OK)
    }


    /**
     * POST  /games : Tworzy Grę
     *
     * @param game Gra do stworzenia
     * @return ResponseEntity ze statusem 201 (Created) i stworzona gra lub status 400 (Bad Request) jeżeli Gra ma już ID
     */
    @PostMapping("/games")
    fun createGame(@Valid @RequestBody game: GameDto): ResponseEntity<GameDto> {
        log.info("REST request to create Game {} ", game)
        if (game.id != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new game cannot already have an ID")).body(null)
        }
        val result = gameService.save(game)
        return ResponseEntity.created(URI("/api/games/${result.id}"))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.id.toString()))
                .body(result)
    }


    /**
     * PUT  /games : Aktualizuje istniejącą gre
     *
     * @param game Gra do modyfikacji
     * @return ResponseEntity ze statusem 200 (OK) i zaktualizowana gra
     * lub status 400 (Bad Request) jeżeli Gra nie jest poprawnie zwalidowana,
     * lub status 500 (Internal Server Error) jeżeli Gra nie może zostać zmodyfikowana
     */
    @PutMapping("/games")
    fun updateGame(@Valid @RequestBody game: GameDto): ResponseEntity<GameDto> {
        log.info("REST request to update Game : {}", game)
        if (game.id == null) {
            return createGame(game)
        }
        val result = gameService.save(game)
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, game.id.toString()))
                .body(result)
    }


    /**
     * DELETE  /games/:id : Usuwa Grę o podanym id
     *
     * @param id Id Gry która ma zostać usunięta
     * @return ResponseEntity ze statusem 200 (OK)
     */
    @DeleteMapping("/games/{id}")
    fun deleteGame(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("REST request to delete Game : {}", id)
        return if(gameService.existsById(id)) {
            gameService.deleteById(id)
            ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build()
        } else {
            log.warn("Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


}
