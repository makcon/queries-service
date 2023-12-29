package makcon.queries.domain.command

import java.time.Instant

data class UpdateObjectCommand<T>(
    val objectId: String,
    val updatedAt: Instant,
    val data: T,
)
