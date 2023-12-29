package makcon.queries.external.product

data class ProductUpdatedEventV1(
    val productId: String,
    val dataPrev: ProductEventDataV1,
    val dataNext: ProductEventDataV1,
)
