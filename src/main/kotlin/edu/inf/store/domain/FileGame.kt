package edu.inf.store.domain

import javax.persistence.*

@Entity
@Table(name = "file_game")
data class FileGame(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "file")
        var file: ByteArray? = null,

        @Column(name = "version")
        var version: String = "1.0"
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileGame

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}