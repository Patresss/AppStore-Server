package edu.inf.store.domain

import javax.persistence.*

@Entity
@Table(name = "game")
data class Game(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "name")
        var name: String = "",

        @Column(name = "description")
        var description: String = "",

        @Column(name = "icon")
        var icon: ByteArray? = null,

        @Column(name = "image")
        var image: ByteArray? = null,

        @Column(name = "version")
        var version: String = "1.0"
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}