package makcon.queries.domain.model

import java.time.Instant

data class EventSync(
    val topic: String,
    val lastCreatedAt: Instant?,
    val synced: Boolean,
)
