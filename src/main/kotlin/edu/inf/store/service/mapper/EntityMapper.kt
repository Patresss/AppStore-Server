package edu.inf.store.service.mapper

import java.util.*

abstract class EntityMapper<EntityType, EntityDtoType> {

    fun toEntity(dtoList: List<EntityDtoType>?): List<EntityType> {
        if (dtoList == null) {
            return ArrayList()
        }

        val list = dtoList.map { toEntity(it) }

        return list
    }

    fun toDto(entityList: List<EntityType>?): List<EntityDtoType> {
        if (entityList == null) {
            return ArrayList()
        }

        val list = entityList.map { toDto(it) }

        return list
    }


    fun toEntity(entityDtoSet: Set<EntityDtoType>?): Set<EntityType> {
        if (entityDtoSet == null) {
            return HashSet()
        }

        val entitySet = entityDtoSet
                .map { toEntity(it) }
                .toSet()

        return entitySet
    }

    fun toDto(entitySet: Set<EntityType>?): Set<EntityDtoType> {
        if (entitySet == null) {
            return HashSet()
        }

        val entityDtoTypes = entitySet
                .map { toDto(it) }
                .toSet()

        return entityDtoTypes
    }


    abstract fun toEntity(entityDto: EntityDtoType): EntityType

    abstract fun toDto(entity: EntityType): EntityDtoType

    open fun toDtoWithoutChildren(it: EntityType): EntityDtoType = toDto(it)

}
