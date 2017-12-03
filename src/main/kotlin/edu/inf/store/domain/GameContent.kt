package edu.inf.store.domain

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "game_content")
data class GameContent(

        @Column(name = "file")
        @get:NotNull
        var file: ByteArray? = null,

        @Column(name = "version")
        var version: String = ""

) : AbstractApplicationEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameContent

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}