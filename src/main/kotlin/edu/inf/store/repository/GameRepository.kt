package edu.inf.store.repository

import edu.inf.store.domain.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query


interface GameRepository : JpaRepository<Game, Long> {

}