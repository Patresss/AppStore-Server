package edu.inf.store.web.rest.resources

import edu.inf.store.service.FileGameService
import edu.inf.store.service.dto.FileGameDto
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
class FileGameResource(private val fileGameService: FileGameService) {

    companion object {
        private val ENTITY_NAME = "FileGame"
        val log: Logger = LoggerFactory.getLogger(FileGameResource::class.java)
    }

    @GetMapping("/file-game/{id}")
    fun getFileGame(@PathVariable id: Long): ResponseEntity<FileGameDto> {
        log.info("REST request to get File Game : {}", id)
        return try {
            val fileGame = fileGameService.findOne(id)
            log.debug("Found File Game : {}", fileGame)
            ResponseEntity(fileGame, HttpStatus.OK)
        } catch (e: Exception) {
            log.warn("File Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/file-game/newest-version/{gameId}")
    fun getNewestVersionOfFileGame(@PathVariable gameId: Long): ResponseEntity<FileGameDto> {
        log.info("REST request to get newest version of File Game : {}", gameId)
        return try {
            val fileGame = fileGameService.findByNewestVersion(gameId)
            log.debug("Newest version of File Game : {}", fileGame)
            ResponseEntity(fileGame, HttpStatus.OK)
        } catch (e: Exception) {
            log.warn("Newest version of File Game not found id : {}", gameId)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/file-game")
    fun createFileGame(@Valid @RequestBody fileGame: FileGameDto): ResponseEntity<FileGameDto> {
        log.info("REST request to create File Game {} ", fileGame)
        fileGame.newestVersion = true
        if (fileGame.id != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new file game cannot already have an ID")).body(null)
        }
        val result = fileGameService.save(fileGame)
        return ResponseEntity.created(URI("/api/file-game/${result.id}"))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.id.toString()))
                .body(result)
    }


    @DeleteMapping("/file-game/{id}")
    fun deleteFileGame(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("REST request to delete File Game : {}", id)
        return if(fileGameService.existsById(id)) {
            fileGameService.deleteById(id)
            ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build()
        } else {
            log.warn("Game not found id : {}", id)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

}