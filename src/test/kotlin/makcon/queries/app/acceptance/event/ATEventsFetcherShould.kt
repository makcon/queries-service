package makcon.queries.app.acceptance.event

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import makcon.queries.adapters.repository.table.eventSyncTable
import makcon.queries.app.acceptance.ATAbstractTest
import makcon.queries.app.event.fetcher.EventsFetcher
import makcon.queries.app.mother.EventV1Mother
import makcon.queries.app.mother.EventsResultV1Mother
import makcon.queries.domain.model.EventSync
import makcon.queries.domain.port.EventSyncRepositoryPort
import makcon.queries.external.event.client.EventsClient
import makcon.queries.external.event.client.EventsClientFactory
import makcon.queries.external.event.constant.Topic.USERS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mybatis.dynamic.sql.util.kotlin.spring.deleteFrom
import org.mybatis.dynamic.sql.util.kotlin.spring.insertInto
import org.springframework.beans.factory.annotation.Autowired

class ATEventsFetcherShould : ATAbstractTest() {
    
    @Autowired
    lateinit var fetcher: EventsFetcher
    
    @Autowired
    lateinit var repository: EventSyncRepositoryPort
    
    @MockkBean
    lateinit var eventsClientFactory: EventsClientFactory
    
    @MockkBean
    lateinit var eventsClient: EventsClient
    
    val givenTopic = USERS
    
    @BeforeEach
    internal fun setUp() {
        template.deleteFrom(eventSyncTable) {}
        
        every { eventsClientFactory.get(any()) } returns eventsClient
    }
    
    @Test
    fun `do nothing when events synced`() {
        // given
        createEventsSync(true)
        
        // when
        fetcher.run()
        
        // then
        repository.findByTopicRequired(givenTopic) shouldBe EventSync(
            topic = givenTopic,
            lastCreatedAt = null,
            synced = true
        )
        verify(exactly = 0) { eventsClient.fetch(any()) }
    }
    
    @Test
    fun `fetch not synced events`() {
        // given
        createEventsSync(false)
        val expectedEvent = EventV1Mother.of()
        val expectedEventsResult = EventsResultV1Mother.of(items = listOf(expectedEvent))
        val expectedEventsResult2 = EventsResultV1Mother.of(items = listOf())
        every { eventsClient.fetch(any()) }.returnsMany(
            expectedEventsResult,
            expectedEventsResult2
        )
        
        // when
        fetcher.run()
        
        // then
        repository.findByTopicRequired(givenTopic) shouldBe EventSync(
            topic = givenTopic,
            lastCreatedAt = expectedEvent.createdAt,
            synced = true
        )
        verify(exactly = 2) { eventsClient.fetch(any()) }
    }
    
    private fun createEventsSync(synced: Boolean) {
        template.insertInto(eventSyncTable) {
            set(eventSyncTable.id) toValue givenTopic
            set(eventSyncTable.synced) toValue synced
        }
    }
}