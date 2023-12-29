package makcon.queries.app.event.fetcher

import kotlinx.coroutines.*
import makcon.queries.domain.model.EventSync
import makcon.queries.domain.port.EventSyncRepositoryPort
import makcon.queries.external.event.client.EventsClientFactory
import makcon.queries.external.event.dto.EventsRequestParamsV1
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class EventsFetcher(
    private val eventSyncRepository: EventSyncRepositoryPort,
    private val eventsClientFactory: EventsClientFactory,
    private val eventsListener: EventListener,
    @Value("\${events.batch.size:\${EVENTS_BATCH_SIZE:200}}")
    private val batchSize: Int,
) {
    
    private val log = KotlinLogging.logger { }
    
    fun run() = runBlocking {
        eventSyncRepository
            .findNotSynced()
            .forEach { launch { sync(it.topic) } }
    }
    
    private fun sync(topic: String) {
        log.info { "Starting to sync, topic: $topic" }
        
        var eventsSize: Int
        var totalEventsSize = 0
        var pageNumber = 0
        val startTime = Instant.now()
        do {
            val eventSync = eventSyncRepository.findByTopicRequired(topic)
            eventsSize = doSync(eventSync, pageNumber)
            pageNumber++
            totalEventsSize += eventsSize
        } while (eventsSize > 0)
        
        log.info {
            "Finished sync, topic: $topic " +
                    "in ${Duration.between(startTime, Instant.now()).toSeconds()} sec " +
                    "total events: $totalEventsSize"
        }
    }
    
    private fun doSync(eventSync: EventSync, pageNumber: Int): Int {
        val params = EventsRequestParamsV1(
            topic = eventSync.topic,
            createdAt = eventSync.lastCreatedAt ?: Instant.EPOCH,
            pageNumber = pageNumber,
            pageSize = batchSize,
        )
        
        val events = eventsClientFactory
            .get(eventSync.topic)
            .fetch(params)
            .items
        
        if (events.isEmpty()) {
            eventSyncRepository.update(eventSync.copy(synced = true))
            return 0
        }
        
        events.forEach { eventsListener.listen(eventSync.topic, it) }
        
        eventSyncRepository.update(eventSync.copy(lastCreatedAt = events.last().createdAt))
        
        return events.size
    }
}