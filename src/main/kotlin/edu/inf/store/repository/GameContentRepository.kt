package edu.inf.store.repository

import edu.inf.store.domain.GameContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GameContentRepository : JpaRepository<GameContent, Long>