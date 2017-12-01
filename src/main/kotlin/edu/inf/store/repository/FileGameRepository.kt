package edu.inf.store.repository

import edu.inf.store.domain.FileGame
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FileGameRepository : JpaRepository<FileGame, Long> {

    @Modifying
    @Query("update FileGame fileGame set fileGame.newestVersion = false where fileGame.game.id = :gameId and fileGame.newestVersion = true")
    fun removeStatusOfNewestVersion(@Param("gameId") gameId: Long?)

}