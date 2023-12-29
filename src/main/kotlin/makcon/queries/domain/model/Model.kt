package makcon.queries.domain.model

import java.time.Instant

data class Model<T>(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val data: T,
)
