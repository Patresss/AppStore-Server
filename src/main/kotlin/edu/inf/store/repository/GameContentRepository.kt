package edu.inf.store.repository

import edu.inf.store.domain.GameContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GameContentRepository : JpaRepository<GameContent, Long> {

    @Modifying
    @Query("update GameContent GameContent set GameContent.newestVersion = false where GameContent.game.id = :gameId and GameContent.newestVersion = true")
    fun removeStatusOfNewestVersion(@Param("gameId") gameId: Long?)

    fun findByGameIdAndNewestVersionTrue(@Param("gameId") gameId: Long) : GameContent
}