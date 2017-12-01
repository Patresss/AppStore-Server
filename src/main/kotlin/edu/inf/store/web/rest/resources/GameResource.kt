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

@RestController
@RequestMapping("/api")
class GameResource(private val gameService: GameService) {

    companion object {
        private val ENTITY_NAME = "Game"
        val log: Logger = LoggerFactory.getLogger(GameResource::class.java)
    }

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

    @GetMapping("/games")
    fun getAllGames(): ResponseEntity<List<GameDto>> {
        log.info("REST request to get all Games")
        val games = gameService.findAllWithoutChildren()
        return ResponseEntity(games, HttpStatus.OK)
    }

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
