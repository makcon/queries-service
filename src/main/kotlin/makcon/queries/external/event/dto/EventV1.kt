package makcon.queries.external.event.dto

import java.time.Instant
import java.util.*

data class EventV1(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Instant = Instant.now(),
    val action: String,
    val body: Any,
    val createdBy: String,
)