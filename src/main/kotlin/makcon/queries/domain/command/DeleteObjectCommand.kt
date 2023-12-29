package makcon.queries.domain.command

import java.time.Instant

data class DeleteObjectCommand(
    val objectId: String,
    val deletedAt: Instant,
)
