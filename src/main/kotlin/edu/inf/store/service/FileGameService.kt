package edu.inf.store.service

import edu.inf.store.domain.FileGame
import edu.inf.store.repository.FileGameRepository
import edu.inf.store.service.dto.FileGameDto
import edu.inf.store.service.mapper.EntityMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class FileGameService(entityRepository: FileGameRepository, entityMapper: EntityMapper<FileGame, FileGameDto>) : EntityService<FileGame, FileGameDto, FileGameRepository>(entityRepository, entityMapper) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(FileGameService::class.java)
    }

    override fun save(entityDto: FileGameDto): FileGameDto {
        if (entityDto.newestVersion) {
            log.debug("Change current newest version to false by game id ${entityDto.gameId}")
            entityRepository.removeStatusOfNewestVersion(entityDto.gameId)
        }
        return super.save(entityDto)
    }

    @Transactional(readOnly = true)
    fun findByNewestVersion(gameId: Long): FileGameDto {
        log.debug("Request to get newest version by game id $gameId")
        val entity = entityRepository.findByGameIdAndNewestVersionTrue(gameId)
        return entityMapper.toDto(entity)
    }

}
