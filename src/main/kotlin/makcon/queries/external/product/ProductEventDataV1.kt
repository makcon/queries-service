package makcon.queries.external.product

data class ProductEventDataV1(
    val name: String,
    val status: String,
    val price: Double,
    val params: Map<String, Any> = mapOf(),
)
