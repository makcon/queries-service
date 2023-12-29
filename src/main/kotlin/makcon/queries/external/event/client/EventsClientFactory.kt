package makcon.queries.external.event.client

import org.springframework.stereotype.Component

@Component
class EventsClientFactory {
    
    fun get(topic: String): EventsClient = FakeEventsClient(topic)
}