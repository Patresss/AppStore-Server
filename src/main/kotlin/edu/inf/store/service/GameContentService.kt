package edu.inf.store.service

import edu.inf.store.domain.GameContent
import edu.inf.store.repository.GameContentRepository
import edu.inf.store.service.dto.GameContentDto
import edu.inf.store.service.mapper.EntityMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class GameContentService(entityRepository: GameContentRepository, entityMapper: EntityMapper<GameContent, GameContentDto>) : EntityService<GameContent, GameContentDto, GameContentRepository>(entityRepository, entityMapper)
