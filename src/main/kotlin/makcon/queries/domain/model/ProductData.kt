package makcon.queries.domain.model

data class ProductData(
    val name: String,
    val status: String,
    val rawData: ProductRawData,
)

data class ProductRawData(
    val price: Double,
    val params: Map<String, Any> = mapOf(),
)