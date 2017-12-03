package edu.inf.store.web.rest.resources

import edu.inf.store.service.GameContentService
import edu.inf.store.service.dto.GameContentDto
import edu.inf.store.web.rest.util.HeaderUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

/**
 * REST controller do zarządzania Zawartości Gry
 */
@RestController
@RequestMapping("/api")
class GameContentResource(private val gameContentService: GameContentService) {

    companion object {
        private val ENTITY_NAME = "GameContent"
        val log: Logger = LoggerFactory.getLogger(GameContentResource::class.java)
    }

    /**
     * GET  /game-contents/:id : Pobiera Zawartość Gry o podanym ID
     *
     * @param id Id Zawartości Gry któa ma zostać zwrócona
     * @return ResponseEntity ze statusem 200 (OK) oraz Zawartość Gry o podanym ID lub status 404 (Not Found)
     */
    @GetMapping("/game-contents/{id}")
    fun getGameContent(@PathVariable id: Long): ResponseEntity<GameContentDto> {
        log.info("REST request to get Game Content : {}", id)
        return try {
            val gameContent = gameContentService.findOne(id)
            log.debug("Found Game Content : {}", gameContent)
            ResponseEntity(gameContent, HttpStatus.OK)
        } catch (e: Exception) {
            log.warn("Game Content not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * POST  /game-contents : Tworzy Zawartość Gry
     *
     * @param gameContent Zawartość Gry do stworzenia
     * @return ResponseEntity ze statusem 201 (Created) i stworzona Zawartość Gry lub status 400 (Bad Request) jeżeli Zawartość Gry ma już ID
     */
    @PostMapping("/game-contents")
    fun createGameContent(@Valid @RequestBody gameContent: GameContentDto): ResponseEntity<GameContentDto> {
        log.info("REST request to create Game Content {} ", gameContent)
        if (gameContent.id != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new Game Content cannot already have an ID")).body(null)
        }
        val result = gameContentService.save(gameContent)
        return ResponseEntity.created(URI("/api/game-contents/${result.id}"))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.id.toString()))
                .body(result)
    }

    /**
     * PUT  /game-contents : Aktualizuje istniejącą Zawartość Gry
     *
     * @param gameContent Zawartość Gry do modyfikacji
     * @return ResponseEntity ze statusem 200 (OK) i zaktualizowana Zawartość Gry
     * lub status 400 (Bad Request) jeżeli Zawartość Gry nie jest poprawnie zwalidowana,
     * lub status 500 (Internal Server Error) jeżeli Zawartość Gry nie może zostać zmodyfikowana
     */
    @PutMapping("/game-contents")
    fun updateGameContent(@Valid @RequestBody gameContent: GameContentDto): ResponseEntity<GameContentDto> {
        GameResource.log.info("REST request to update Game Content : {}", gameContent)
        if (gameContent.id == null) {
            return createGameContent(gameContent)
        }
        val result = gameContentService.save(gameContent)
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(GameContentResource.ENTITY_NAME, gameContent.id.toString()))
                .body(result)
    }


    /**
     * DELETE  /game-contents/:id : Usuwa Zawartość Gry o podanym id
     *
     * @param id Id Zawartości Gry która ma zostać usunięta
     * @return ResponseEntity ze statusem 200 (OK)
     */
    @DeleteMapping("/game-contents/{id}")
    fun deleteGameContent(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("REST request to delete Game Content : {}", id)
        return if(gameContentService.existsById(id)) {
            gameContentService.deleteById(id)
            ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build()
        } else {
            log.warn("Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

}