package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.EventSync
import java.time.Instant

object EventSyncMother {
    
    fun of(
        topic: String = Rand.string(),
        lastCreatedAt: Instant? = Rand.instantPast(),
        synced: Boolean = Rand.bool(),
    ) = EventSync(
        topic = topic,
        lastCreatedAt = lastCreatedAt,
        synced = synced
    )
}