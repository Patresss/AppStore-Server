package edu.inf.store.domain

import javax.persistence.*

@Entity
@Table(name = "game")
data class Game(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "name")
        var name: String = ""
) {
    constructor() : this(id = null)
}
