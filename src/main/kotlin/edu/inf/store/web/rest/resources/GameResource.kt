package edu.inf.store.web.rest.resources

import edu.inf.store.domain.Game
import edu.inf.store.repository.GameRepository
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
class GameResource(private val gameRepository: GameRepository) {

    companion object {
        private val ENTITY_NAME = "Game"
        val log: Logger = LoggerFactory.getLogger(GameResource::class.java)
    }

    @GetMapping("/games/{id}")
    fun getGame(@PathVariable id: Long): ResponseEntity<Game> {
        log.info("REST request to get Game : {}", id)
        return try {
            val game = gameRepository.getOne(id)
            log.debug("Found Game : {}", game)
            ResponseEntity(game, HttpStatus.OK)
        } catch (e: Exception) {
            log.warn("Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/games")
    fun getAllGames(): ResponseEntity<List<Game>> {
        log.info("REST request to get all Games")
        val games = gameRepository.findAll()
        return ResponseEntity(games, HttpStatus.OK)
    }

    @PostMapping("/games")
    fun createGame(@Valid @RequestBody game: Game): ResponseEntity<Game> {
        log.info("REST request to create Game {} ", game)
        if (game.id != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new game cannot already have an ID")).body(null)
        }
        val result = gameRepository.save(game)
        return ResponseEntity.created(URI("/api/games/${result.id}"))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.id.toString()))
                .body(result)
    }

    @PutMapping("/games")
    fun updateGame(@Valid @RequestBody game: Game): ResponseEntity<Game> {
        log.info("REST request to update Game : {}", game)
        if (game.id == null) {
            return createGame(game)
        }
        val result = gameRepository.save(game)
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, game.id.toString()))
                .body(result)
    }

    @DeleteMapping("/games/{id}")
    fun deleteLesson(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("REST request to delete Game : {}", id)
        return if(gameRepository.existsById(id)) {
            gameRepository.deleteById(id)
            ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build()
        } else {
            log.warn("Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


}
