package makcon.queries.domain.model

data class OrderData(
    val productId: String,
    val userId: String,
    val status: String,
    val name: String,
    val rawData: OrderRawData,
)

data class OrderRawData(
    val quantity: Int,
    val address: String,
    val comment: String?,
)
