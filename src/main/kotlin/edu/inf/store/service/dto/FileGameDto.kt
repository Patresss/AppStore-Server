package edu.inf.store.service.dto

import javax.validation.constraints.NotNull

data class GameContentDto(

        var id: Long? = null,
        @get:NotNull
        var gameId: Long? = null,
        @get:NotNull
        var file: ByteArray? = null,
        var version: String = "",
        var newestVersion: Boolean = true
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameContentDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}