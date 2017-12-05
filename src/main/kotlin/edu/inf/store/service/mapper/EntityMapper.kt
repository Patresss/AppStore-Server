package edu.inf.store.service.mapper

import edu.inf.store.domain.Game
import edu.inf.store.service.dto.GameDto

interface  EntityMapper<EntityType, EntityDtoType> {

     fun toEntity(entityDto: EntityDtoType): EntityType

     fun toDto(entity: EntityType): EntityDtoType

     fun toListObjectDto(it: Game): GameDto
}
