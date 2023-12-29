package makcon.queries.dto

import java.time.Instant

data class OrderV1(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val userId: String,
    val productId: String,
    val name: String,
    val status: String,
    val quantity: Int,
    val address: String,
    val comment: String?,
)