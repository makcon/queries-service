package makcon.queries.dto

import java.time.Instant

data class UserV1(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val firstName: String,
    val lastName: String,
    val email: String,
)