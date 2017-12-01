package edu.inf.store.service.mapper

import edu.inf.store.domain.FileGame
import edu.inf.store.repository.GameRepository
import edu.inf.store.service.dto.FileGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileGameMapper : EntityMapper<FileGame, FileGameDto>() {

    @Autowired
    lateinit private var gameRepository: GameRepository

    override fun toEntity(entityDto: FileGameDto): FileGame {
        return FileGame()
                .apply {
                    id = entityDto.id
                    game = gameRepository.getOne(entityDto.gameId)
                    file = entityDto.file
                    version = entityDto.version
                    newestVersion = entityDto.newestVersion
                }
    }

    override fun toDto(entity: FileGame): FileGameDto {
        return FileGameDto()
                .apply {
                    id = entity.id
                    gameId = entity.game?.id
                    file = entity.file
                    version = entity.version
                    newestVersion = entity.newestVersion
                }
    }
}
