package makcon.queries.domain.port

import makcon.queries.domain.exception.ModelNotFoundException
import makcon.queries.domain.model.EventSync

interface EventSyncRepositoryPort {
    
    fun findNotSynced(): List<EventSync>
    
    fun findByTopic(topic: String): EventSync?
    
    fun update(eventSync: EventSync)
    
    fun findByTopicRequired(topic: String): EventSync = findByTopic(topic)
        ?: throw ModelNotFoundException(EventSync::class, topic)
}