package edu.inf.store.domain

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "game")
data class Game(

        @Column(name = "name")
        @get:NotNull
        var name: String = "",

        @Column(name = "description")
        var description: String = "",

        @Column(name = "icon")
        var icon: ByteArray? = null,

        @Column(name = "image")
        var image: ByteArray? = null,

        @Column(name = "version")
        var version: String = "",

        @Column(name = "file")
        @get:NotNull
        var file: ByteArray? = null


) : AbstractApplicationEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}