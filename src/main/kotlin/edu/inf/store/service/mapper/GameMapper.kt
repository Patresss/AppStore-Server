package edu.inf.store.service.mapper

import edu.inf.store.domain.Game
import edu.inf.store.service.dto.GameDto
import org.springframework.stereotype.Service

@Service
open class GameMapper(var fileGameMapper: FileGameMapper) : EntityMapper<Game, GameDto>() {



    override fun toEntity(entityDto: GameDto): Game {
        return Game()
                .apply {
                    id = entityDto.id
                    name = entityDto.name
                    description = entityDto.description
                    icon = entityDto.icon
                    image = entityDto.image
                    fileGames = entityDto.fileGames.map { fileGameMapper.toEntity(it) }
                }
    }

    override fun toDto(entity: Game): GameDto {
        return GameDto()
            .apply {
                id = entity.id
                name = entity.name
                description = entity.description
                icon = entity.icon
                image = entity.image
                fileGames = entity.fileGames.map { fileGameMapper.toDto(it) }
            }
    }

    override fun toDtoWithoutChildren(it: Game): GameDto {
        return GameDto()
                .apply {
                    id = it.id
                    name = it.name
                    description = it.description
                    icon = it.icon
                    image = it.image
                    fileGamesVersionName = it.fileGames.map { it.version }
                }
    }

}
