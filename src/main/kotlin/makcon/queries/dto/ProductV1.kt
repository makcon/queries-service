package makcon.queries.dto

import java.time.Instant

data class ProductV1(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val price: Double,
    val status: String,
    val name: String,
    val params: Map<String, Any> = mapOf(),
)