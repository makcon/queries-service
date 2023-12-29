package makcon.queries.app.event.fetcher

import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import makcon.queries.app.mother.EventSyncMother
import makcon.queries.app.mother.EventV1Mother
import makcon.queries.app.mother.EventsResultV1Mother
import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.EventSync
import makcon.queries.domain.port.EventSyncRepositoryPort
import makcon.queries.external.event.client.EventsClient
import makcon.queries.external.event.client.EventsClientFactory
import makcon.queries.external.event.dto.EventsRequestParamsV1
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(MockKExtension::class)
internal class EventsFetcherShould {
    
    @InjectMockKs
    lateinit var fetcher: EventsFetcher
    
    @RelaxedMockK
    lateinit var eventSyncRepository: EventSyncRepositoryPort
    
    @MockK
    lateinit var eventsClientFactory: EventsClientFactory
    
    @MockK
    lateinit var eventsClient: EventsClient
    
    @RelaxedMockK
    lateinit var eventsListener: EventListener
    
    private val batchSize: Int = Rand.int()
    private val eventsRequestSlot = slot<EventsRequestParamsV1>()
    private val eventsRequestSlot2 = slot<EventsRequestParamsV1>()
    private val eventsRequestSlot3 = slot<EventsRequestParamsV1>()
    private val eventSyncUpdatedSlot = slot<EventSync>()
    private val eventSyncUpdatedSlot2 = slot<EventSync>()
    private val eventSyncUpdatedSlot3 = slot<EventSync>()
    private val storedEventSync: EventSync = EventSyncMother.of(synced = false)
    
    @BeforeEach
    internal fun setUp() {
        fetcher = EventsFetcher(
            eventSyncRepository = eventSyncRepository,
            eventsClientFactory = eventsClientFactory,
            eventsListener = eventsListener,
            batchSize = batchSize,
        )
        
        every { eventsClientFactory.get(any()) } returns eventsClient
    }
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(
            eventSyncRepository,
            eventsClientFactory,
            eventsListener,
        )
    }
    
    @Test
    fun `do nothing when all events are synced`() {
        // given
        every { eventSyncRepository.findNotSynced() } returns listOf()
        
        // when
        fetcher.run()
        
        // then
        verify(exactly = 1) { eventSyncRepository.findNotSynced() }
    }
    
    @Test
    fun `sync in one round when all events are not synced`() {
        // given
        val expectedEvent = EventV1Mother.of()
        val expectedEventsResult = EventsResultV1Mother.of(items = listOf(expectedEvent))
        val expectedEventsResult2 = EventsResultV1Mother.of(items = listOf())
        every { eventSyncRepository.findNotSynced() } returns listOf(storedEventSync)
        every { eventSyncRepository.findByTopicRequired(any()) } returns storedEventSync
        every { eventsClient.fetch(any()) }.returnsMany(
            expectedEventsResult,
            expectedEventsResult2,
        )
        
        // when
        fetcher.run()
        
        // then
        verify(exactly = 1) { eventSyncRepository.findNotSynced() }
        verifyOrder {
            eventsClient.fetch(capture(eventsRequestSlot))
            eventsClient.fetch(capture(eventsRequestSlot2))
        }
        verifyEventsRequest(eventsRequestSlot, storedEventSync, 0)
        verifyEventsRequest(eventsRequestSlot2, storedEventSync, 1)
        verify(exactly = 2) { eventsClientFactory.get(storedEventSync.topic) }
        verify(exactly = 2) { eventSyncRepository.findByTopicRequired(storedEventSync.topic) }
        verify(exactly = 1) { eventsListener.listen(storedEventSync.topic, expectedEvent) }
        verifyOrder {
            eventSyncRepository.update(capture(eventSyncUpdatedSlot))
            eventSyncRepository.update(capture(eventSyncUpdatedSlot2))
        }
        verifyEventSync(eventSyncUpdatedSlot, expectedEvent.createdAt, false)
        verifyEventSync(eventSyncUpdatedSlot2, storedEventSync.lastCreatedAt, true)
    }
    
    @Test
    fun `sync in two rounds when all events are not synced`() {
        // given
        val expectedEvent = EventV1Mother.of()
        val expectedEvent2 = EventV1Mother.of()
        val expectedEventsResult = EventsResultV1Mother.of(items = listOf(expectedEvent))
        val expectedEventsResult2 = EventsResultV1Mother.of(items = listOf(expectedEvent2))
        val expectedEventsResult3 = EventsResultV1Mother.of(items = listOf())
        every { eventSyncRepository.findNotSynced() } returns listOf(storedEventSync)
        every { eventSyncRepository.findByTopicRequired(any()) } returns storedEventSync
        every { eventsClient.fetch(any()) }.returnsMany(
            expectedEventsResult,
            expectedEventsResult2,
            expectedEventsResult3,
        )
        
        // when
        fetcher.run()
        
        // then
        verify(exactly = 1) { eventSyncRepository.findNotSynced() }
        verifyOrder {
            eventsClient.fetch(capture(eventsRequestSlot))
            eventsClient.fetch(capture(eventsRequestSlot2))
            eventsClient.fetch(capture(eventsRequestSlot3))
        }
        verifyEventsRequest(eventsRequestSlot, storedEventSync, 0)
        verifyEventsRequest(eventsRequestSlot2, storedEventSync, 1)
        verifyEventsRequest(eventsRequestSlot3, storedEventSync, 2)
        verify(exactly = 3) { eventsClientFactory.get(storedEventSync.topic) }
        verify(exactly = 3) { eventSyncRepository.findByTopicRequired(storedEventSync.topic) }
        verifyOrder {
            eventsListener.listen(storedEventSync.topic, expectedEvent)
            eventsListener.listen(storedEventSync.topic, expectedEvent2)
        }
        verifyOrder {
            eventSyncRepository.update(capture(eventSyncUpdatedSlot))
            eventSyncRepository.update(capture(eventSyncUpdatedSlot2))
            eventSyncRepository.update(capture(eventSyncUpdatedSlot3))
        }
        verifyEventSync(eventSyncUpdatedSlot, expectedEvent.createdAt, false)
        verifyEventSync(eventSyncUpdatedSlot2, expectedEvent2.createdAt, false)
        verifyEventSync(eventSyncUpdatedSlot3, storedEventSync.lastCreatedAt, true)
    }
    
    private fun verifyEventsRequest(
        slot: CapturingSlot<EventsRequestParamsV1>,
        storedEventSync: EventSync,
        pageNumber: Int,
    ) {
        slot.captured shouldBe EventsRequestParamsV1(
            topic = storedEventSync.topic,
            createdAt = storedEventSync.lastCreatedAt!!,
            pageNumber = pageNumber,
            pageSize = batchSize,
        )
    }
    
    private fun verifyEventSync(
        slot: CapturingSlot<EventSync>,
        lastCreatedAt: Instant?,
        synced: Boolean,
    ) {
        slot.captured shouldBe EventSync(
            topic = storedEventSync.topic,
            lastCreatedAt = lastCreatedAt,
            synced = synced
        )
    }
}