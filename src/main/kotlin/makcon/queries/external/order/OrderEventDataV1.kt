package makcon.queries.external.order

data class OrderEventDataV1(
    val productId: String,
    val userId: String,
    val name: String,
    val status: String,
    val quantity: Int,
    val address: String,
    val comment: String?,
)
