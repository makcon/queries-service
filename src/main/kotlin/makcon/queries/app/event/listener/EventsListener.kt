package makcon.queries.app.event.listener

import makcon.queries.external.event.dto.EventV1

interface EventsListener {
    
    fun listen(event: EventV1)
}