package edu.inf.store.service.mapper

import edu.inf.store.domain.Game
import edu.inf.store.service.dto.GameDto
import org.springframework.stereotype.Service

@Service
class GameMapper(var gameContentMapper: GameContentMapper) : EntityMapper<Game, GameDto>() {



    override fun toEntity(entityDto: GameDto): Game {
        return Game()
                .apply {
                    id = entityDto.id
                    name = entityDto.name
                    description = entityDto.description
                    icon = entityDto.icon
                    image = entityDto.image
                    GameContents = entityDto.GameContents.map { gameContentMapper.toEntity(it) }
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
                GameContents = entity.GameContents.map { gameContentMapper.toDto(it) }
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
                }
    }

}
