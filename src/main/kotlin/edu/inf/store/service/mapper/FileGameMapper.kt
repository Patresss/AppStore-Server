package edu.inf.store.service.mapper

import edu.inf.store.domain.GameContent
import edu.inf.store.repository.GameRepository
import edu.inf.store.service.dto.GameContentDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameContentMapper : EntityMapper<GameContent, GameContentDto>() {

    @Autowired
    lateinit private var gameRepository: GameRepository

    override fun toEntity(entityDto: GameContentDto): GameContent {
        return GameContent()
                .apply {
                    id = entityDto.id
                    game = gameRepository.getOne(entityDto.gameId)
                    file = entityDto.file
                    version = entityDto.version
                    newestVersion = entityDto.newestVersion
                }
    }

    override fun toDto(entity: GameContent): GameContentDto {
        return GameContentDto()
                .apply {
                    id = entity.id
                    gameId = entity.game?.id
                    file = entity.file
                    version = entity.version
                    newestVersion = entity.newestVersion
                }
    }
}
