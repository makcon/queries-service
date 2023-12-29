package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.event.dto.EventsResultV1
import java.time.Instant

object EventV1Mother {
    
    fun of(
        action: String = Rand.string(),
        body: Any = Rand.string(),
        createdAt: Instant = Rand.instantNow(),
        createdBy: String = Rand.string(),
        id: String = Rand.string(),
    ) = EventV1(
        id = id,
        action = action,
        body = body,
        createdAt = createdAt,
        createdBy = createdBy,
    )
}

object EventsResultV1Mother {
    
    fun of(
        totalCount: Long = Rand.long(),
        pageNumber: Int = Rand.int(),
        pageSize: Int = Rand.int(),
        items: List<EventV1> = listOf(EventV1Mother.of()),
    ) = EventsResultV1(
        totalCount = totalCount,
        pageNumber = pageNumber,
        pageSize = pageSize,
        items = items
    )
}