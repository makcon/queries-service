package makcon.queries.external.event.client

import makcon.queries.external.event.dto.EventsRequestParamsV1
import makcon.queries.external.event.dto.EventsResultV1

interface EventsClient {
    
    fun fetch(params: EventsRequestParamsV1): EventsResultV1
}