package edu.inf.store.service.dto

import javax.validation.constraints.NotNull

data class GameDto(

        var id: Long? = null,
        @get:NotNull
        var name: String = "",
        var description: String = "",
        var icon: ByteArray? = null,
        var image: ByteArray? = null,
        var file: ByteArray? = null,
        var version: String = ""
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}