package makcon.queries.domain.command

import java.time.Instant

data class CreateObjectCommand<T>(
    val objectId: String,
    val createdAt: Instant,
    val data: T,
)
