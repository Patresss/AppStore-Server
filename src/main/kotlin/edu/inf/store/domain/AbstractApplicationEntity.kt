package edu.inf.store.domain

import javax.persistence.*
import java.io.Serializable
import java.util.Objects

@MappedSuperclass
abstract class AbstractApplicationEntity : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val entity = other as AbstractApplicationEntity?
        if (entity!!.id == null || id == null) {
            return false
        }
        return id == entity.id
    }

    override fun hashCode(): Int = Objects.hashCode(id)

    companion object {
        private const val serialVersionUID = 7296270053887329327L
    }

}
