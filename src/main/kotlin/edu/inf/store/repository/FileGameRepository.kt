package edu.inf.store.repository

import edu.inf.store.domain.FileGame
import edu.inf.store.domain.Game
import org.springframework.data.jpa.repository.JpaRepository

interface FileGameRepository : JpaRepository<FileGame, Long>