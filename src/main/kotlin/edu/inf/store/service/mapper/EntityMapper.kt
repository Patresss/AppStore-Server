package edu.inf.store.service.mapper

abstract class EntityMapper<EntityType, EntityDtoType> {

    abstract fun toEntity(entityDto: EntityDtoType): EntityType

    abstract fun toDto(entity: EntityType): EntityDtoType

    open fun toDtoWithoutChildren(it: EntityType): EntityDtoType = toDto(it)

}
