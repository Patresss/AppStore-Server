package edu.inf.store.repository

import edu.inf.store.domain.Game
import org.springframework.data.jpa.repository.JpaRepository


interface GameRepository : JpaRepository<Game, Long>