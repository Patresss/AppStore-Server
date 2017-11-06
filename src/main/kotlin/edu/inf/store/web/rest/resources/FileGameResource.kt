package edu.inf.store.web.rest.resources

import edu.inf.store.repository.FileGameRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class FileGameResource(private val fileGameRepository: FileGameRepository) {

    companion object {
        private val ENTITY_NAME = "FileGame"
        val log: Logger = LoggerFactory.getLogger(FileGameResource::class.java)
    }

}