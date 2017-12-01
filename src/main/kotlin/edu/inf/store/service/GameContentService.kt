package edu.inf.store.service

import edu.inf.store.domain.GameContent
import edu.inf.store.repository.GameContentRepository
import edu.inf.store.service.dto.GameContentDto
import edu.inf.store.service.mapper.EntityMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class GameContentService(entityRepository: GameContentRepository, entityMapper: EntityMapper<GameContent, GameContentDto>) : EntityService<GameContent, GameContentDto, GameContentRepository>(entityRepository, entityMapper) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GameContentService::class.java)
    }

    override fun save(entityDto: GameContentDto): GameContentDto {
        if (entityDto.newestVersion) {
            log.debug("Change current newest version to false by game id ${entityDto.gameId}")
            entityRepository.removeStatusOfNewestVersion(entityDto.gameId)
        }
        return super.save(entityDto)
    }

    @Transactional(readOnly = true)
    fun findByNewestVersion(gameId: Long): GameContentDto {
        log.debug("Request to get newest version by game id $gameId")
        val entity = entityRepository.findByGameIdAndNewestVersionTrue(gameId)
        return entityMapper.toDto(entity)
    }

}
