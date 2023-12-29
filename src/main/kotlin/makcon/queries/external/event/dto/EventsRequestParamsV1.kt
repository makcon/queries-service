package makcon.queries.external.event.dto

import java.time.Instant

data class EventsRequestParamsV1(
    val topic: String,
    val createdAt: Instant = Instant.EPOCH,
    val pageNumber: Int = 0,
    val pageSize: Int = 100,
)
